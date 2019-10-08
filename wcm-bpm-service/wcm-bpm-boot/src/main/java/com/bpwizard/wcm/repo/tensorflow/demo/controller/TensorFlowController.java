package com.bpwizard.wcm.repo.tensorflow.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.tensorflow.DataType;
//import org.tensorflow.Graph;
//import org.tensorflow.Operation;
//import org.tensorflow.SavedModelBundle;
//import org.tensorflow.Session;
//import org.tensorflow.Tensor;

@RestController
@RequestMapping("/tensorflow/api/demo")
public class TensorFlowController {
	
	@GetMapping("/runGraph")
	public void runGraph(String[] args) {
//		Graph graph = this.createGraph();
//		Object result = this.runGraph(graph, 3.0, 6.0);
//		System.out.println(result);
//		graph.close();
	}
	
	@GetMapping("/savedModel")
	public void savedModel() {
//		SavedModelBundle model = SavedModelBundle.load("./tensor-flow/data/demo", "serve");
//		Tensor<Integer> tensor = model.session().runner().fetch("z").feed("x", Tensor.<Integer>create(3, Integer.class))
//				.feed("y", Tensor.<Integer>create(3, Integer.class)).run().get(0).expect(Integer.class);
//		System.out.println(tensor.intValue());
	}
	
//	private Graph createGraph() {
//		Graph graph = new Graph();
//		Operation a = graph.opBuilder("Const", "a").setAttr("dtype", DataType.fromClass(Double.class))
//				.setAttr("value", Tensor.<Double>create(3.0, Double.class)).build();
//		Operation b = graph.opBuilder("Const", "b").setAttr("dtype", DataType.fromClass(Double.class))
//				.setAttr("value", Tensor.<Double>create(2.0, Double.class)).build();
//		Operation x = graph.opBuilder("Placeholder", "x").setAttr("dtype", DataType.fromClass(Double.class)).build();
//		Operation y = graph.opBuilder("Placeholder", "y").setAttr("dtype", DataType.fromClass(Double.class)).build();
//		Operation ax = graph.opBuilder("Mul", "ax").addInput(a.output(0)).addInput(x.output(0)).build();
//		Operation by = graph.opBuilder("Mul", "by").addInput(b.output(0)).addInput(y.output(0)).build();
//		graph.opBuilder("Add", "z").addInput(ax.output(0)).addInput(by.output(0)).build();
//		return graph;
//	}
//
//	
//	private Object runGraph(Graph graph, Double x, Double y) {
//		Object result;
//		try (Session sess = new Session(graph)) {
//			result = sess.runner().fetch("z").feed("x", Tensor.<Double>create(x, Double.class))
//					.feed("y", Tensor.<Double>create(y, Double.class)).run().get(0).expect(Double.class)
//					.doubleValue();
//		}
//		return result;
//	}	
}