package io.flowing.retail.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.flowing.retail.checkout.domain.Customer;
import io.flowing.retail.checkout.domain.Order;
import io.flowing.retail.checkout.message.Message;
import io.flowing.retail.checkout.message.MessageSender;

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

		Message<Order> message = new Message<Order>("OrderPlacedEvent", order);
		messageSender.send(message);

		// note that we cannot easily return an order id here - as everything is
		// asynchronous
		// and blocking the client is not what we want.
		// but we return an own correlationId which can be used in the UI to show status
		// maybe later
		return "{\"traceId\": \"" + message.getTraceId() + "\"}";
	}

}