package com.bpwizard.bpm.task.send_receive;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.bpm.task.send_receive.domain.Order;
import com.bpwizard.bpm.task.send_receive.message.Message;
import com.bpwizard.bpm.task.send_receive.message.MessageSender;
import com.bpwizard.bpm.task.send_receive.persistence.OrderRepository;

@Component
public class FetchGoodsAdapter implements JavaDelegate {

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public void execute(DelegateExecution context) throws Exception {
		Order order = orderRepository.getOrder((String) context.getVariable("orderId"));
		String traceId = context.getProcessBusinessKey();

		// publish
		messageSender.send(new Message<FetchGoodsCommandPayload>(
			"FetchGoodsCommand", 
			traceId,
			new FetchGoodsCommandPayload().setRefId(order.getId()).setItems(order.getItems())));
	}

}
