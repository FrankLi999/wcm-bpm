package com.bpwizard.wcm.repo.tensorflow.jet.controller;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Int64Value;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.ContextFactory;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tensorflow.framework.TensorProto;
import org.tensorflow.framework.TensorShapeProto;
import com.bpwizard.wcm.repo.tensorflow.jet.controller.support.WordIndex;
import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;
import tensorflow.serving.PredictionServiceGrpc.PredictionServiceFutureStub;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static com.hazelcast.jet.datamodel.Tuple2.tuple2;

/**
 * Shows how to enrich a stream of movie reviews with classification using a
 * pre-trained TensorFlow model. Executes the TensorFlow model using gRPC calls
 * to a TensorFlow Model Server.
 */
@RestController
@RequestMapping("/tensorflow/api/modelserver")
public class ModelServerClassificationController {
	@Autowired
	JetInstance instance;

	private static Pipeline buildPipeline(String dataPath, String serverAddress, IMap<Long, String> reviewsMap) {
		WordIndex wordIndex = new WordIndex(dataPath);
		ContextFactory<PredictionServiceFutureStub> tfServingContext = ContextFactory.withCreateFn(jet -> {
			ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress).usePlaintext().build();
			return PredictionServiceGrpc.newFutureStub(channel);
		}).withDestroyFn(stub -> ((ManagedChannel) stub.getChannel()).shutdownNow()).withLocalSharing()
				.withMaxPendingCallsPerProcessor(16);

		Pipeline p = Pipeline.create();
		p.drawFrom(Sources.map(reviewsMap)).map(Map.Entry::getValue)
				.mapUsingContextAsync(tfServingContext, (stub, review) -> {
					float[][] featuresTensorData = wordIndex.createTensorInput(review);
					TensorProto.Builder featuresTensorBuilder = TensorProto.newBuilder();
					for (float[] featuresTensorDatum : featuresTensorData) {
						for (float v : featuresTensorDatum) {
							featuresTensorBuilder.addFloatVal(v);
						}
					}
					TensorShapeProto.Dim featuresDim1 = TensorShapeProto.Dim.newBuilder()
							.setSize(featuresTensorData.length).build();
					TensorShapeProto.Dim featuresDim2 = TensorShapeProto.Dim.newBuilder()
							.setSize(featuresTensorData[0].length).build();
					TensorShapeProto featuresShape = TensorShapeProto.newBuilder().addDim(featuresDim1)
							.addDim(featuresDim2).build();
					featuresTensorBuilder.setDtype(org.tensorflow.framework.DataType.DT_FLOAT)
							.setTensorShape(featuresShape);
					TensorProto featuresTensorProto = featuresTensorBuilder.build();

					// Generate gRPC request
					Int64Value version = Int64Value.newBuilder().setValue(1).build();
					Model.ModelSpec modelSpec = Model.ModelSpec.newBuilder().setName("reviewSentiment")
							.setVersion(version).build();
					Predict.PredictRequest request = Predict.PredictRequest.newBuilder().setModelSpec(modelSpec)
							.putInputs("input_review", featuresTensorProto).build();

					return toCompletableFuture(stub.predict(request)).thenApply(response -> {
						float classification = response.getOutputsOrThrow("dense_1/Sigmoid:0").getFloatVal(0);
						// emit the review along with the classification
						return tuple2(review, classification);
					});
				}).setLocalParallelism(1) // one worker is enough to drive they async calls
				.drainTo(Sinks.logger());
		return p;
	}

	@GetMapping("/newJob")
	public String newJob(@RequestParam(
			name = "dataPath", required = true) String dataPath,
			@RequestParam(name = "server", required = true) String serverAddress) {

		IMap<Long, String> reviewsMap = instance.getMap("reviewsMap");
		SampleReviews.populateReviewsMap(reviewsMap);
		Pipeline p = buildPipeline(dataPath, serverAddress, reviewsMap);
		instance.newJob(p).join();
		return "done";
	}

	/**
	 * Adapt a {@link ListenableFuture} to java standard {@link CompletableFuture},
	 * which is used by Jet.
	 */
	private static <T> CompletableFuture<T> toCompletableFuture(ListenableFuture<T> lf) {
		CompletableFuture<T> f = new CompletableFuture<>();
		// note that we don't handle CompletableFuture.cancel()
		Futures.addCallback(lf, new FutureCallback<T>() {
			@Override
			public void onSuccess(@NullableDecl T result) {
				f.complete(result);
			}

			@Override
			public void onFailure(Throwable t) {
				f.completeExceptionally(t);
			}
		}, directExecutor());
		return f;
	}
}
