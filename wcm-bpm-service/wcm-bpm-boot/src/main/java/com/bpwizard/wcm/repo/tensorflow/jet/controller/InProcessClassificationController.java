package com.bpwizard.wcm.repo.tensorflow.jet.controller;

import static com.hazelcast.jet.datamodel.Tuple2.tuple2;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;

import com.bpwizard.wcm.repo.tensorflow.jet.controller.support.WordIndex;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.datamodel.Tuple2;
import com.hazelcast.jet.pipeline.ContextFactory;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;

/**
 * Shows how to enrich a stream of movie reviews with classification using a
 * pre-trained TensorFlow model. Executes the TensorFlow model using the
 * in-process method. TensorFlow Model Server execution.
 */
@RestController
@RequestMapping("/tensorflow/api/inprocess")
public class InProcessClassificationController {
	@Autowired
	JetInstance instance;

	@GetMapping("/newJob")
	public String newJob(@RequestParam(name = "dataPath", required = true) String dataPath) {
		// System.setProperty("hazelcast.logging.type", "slf4j");

		IMap<Long, String> reviewsMap = instance.getMap("reviewsMap");
		SampleReviews.populateReviewsMap(reviewsMap);
		instance.newJob(buildPipeline(dataPath, reviewsMap)).join();
		return "done";
	}

	private Pipeline buildPipeline(String dataPath, IMap<Long, String> reviewsMap) {
		WordIndex wordIndex = new WordIndex(dataPath);
		// Set up the mapping context that loads the model on each member, shared
		// by all parallel processors on that member. The path must be available on
		// all members.
		//dataPath should be /tensor-flow/data
		ContextFactory<SavedModelBundle> modelContext = ContextFactory
				.withCreateFn(jet -> SavedModelBundle.load(dataPath + "/model/1", "serve")).withLocalSharing()
				.withDestroyFn(SavedModelBundle::close);
		Pipeline p = Pipeline.create();
		p.drawFrom(Sources.map(reviewsMap)).map(Map.Entry::getValue)
				.mapUsingContext(modelContext, (model, review) -> classify(review, model, wordIndex))
				// TensorFlow executes models in parallel, we'll use 2 local threads to maximize
				// throughput.
				.setLocalParallelism(2).drainTo(
						Sinks.logger(t -> String.format("Sentiment rating for review \"%s\" is %.2f", t.f0(), t.f1())));
		return p;
	}

	private Tuple2<String, Float> classify(String review, SavedModelBundle model, WordIndex wordIndex) {
		try (Tensor<Float> input = Tensors.create(wordIndex.createTensorInput(review));
				Tensor<?> output = model.session().runner().feed("embedding_input:0", input).fetch("dense_1/Sigmoid:0")
						.run().get(0)) {
			float[][] result = new float[1][1];
			output.copyTo(result);
			return tuple2(review, result[0][0]);
		}
	}
}
