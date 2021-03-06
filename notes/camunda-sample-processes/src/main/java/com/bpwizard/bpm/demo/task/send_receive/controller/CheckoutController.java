package com.bpwizard.bpm.demo.task.send_receive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.bpm.demo.task.send_receive.domain.Customer;
import com.bpwizard.bpm.demo.task.send_receive.domain.Order;
import com.bpwizard.bpm.demo.task.send_receive.message.MessageSender;
import com.bpwizard.bpm.demo.task.send_receive.message.PlaceOrderMessage;

@RestController
public class CheckoutController {

	@Autowired
	private MessageSender messageSender;

	@PutMapping(path = "/api/cart/order")
	public String placeOrder(@RequestParam(value = "customerId") String customerId) {

		Order order = new Order();
		order.addItem("article1", 5);
		order.addItem("article2", 10);

		order.setCustomer(new Customer("Camunda", "Zossener Strasse 55\n10961 Berlin\nGermany"));

		PlaceOrderMessage<Order> message = new PlaceOrderMessage<Order>("OrderPlacedEvent", order);
		messageSender.sendPlaceOrderMessage(message);

		// note that we cannot easily return an order id here - as everything is
		// asynchronous
		// and blocking the client is not what we want.
		// but we return an own correlationId which can be used in the UI to show status
		// maybe later
		return "{\"traceId\": \"" + message.getTraceId() + "\"}";
	}

}