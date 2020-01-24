package com.bpwizard.bpm.task.send_receive.base;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.bpm.task.send_receive.domain.Order;
import com.bpwizard.bpm.task.send_receive.FetchGoodsCommandPayload;
import com.bpwizard.bpm.task.send_receive.message.Message;
import com.bpwizard.bpm.task.send_receive.message.MessageSender;
import com.bpwizard.bpm.task.send_receive.persistence.OrderRepository;

/**
 * Alternative implementation if you prefer having send/receive in one single ServiceTask
 * which is often easier understood by "normal people"
 *
 */
@Component
public class FetchGoodsPubSubAdapter extends PublishSubscribeAdapter {
  
  @Autowired
  private MessageSender messageSender;  

  @Autowired
  private OrderRepository orderRepository;  

  @Override
  public void execute(ActivityExecution context) throws Exception {
    Order order = orderRepository.getOrder( //
        (String)context.getVariable("orderId")); 
    String traceId = context.getProcessBusinessKey();

    // publish
    messageSender.send(new Message<FetchGoodsCommandPayload>( //
            "FetchGoodsCommand", //
            traceId, //
            new FetchGoodsCommandPayload() //
              .setRefId(order.getId()) //
              .setItems(order.getItems())));
    
    addMessageSubscription(context, "GoodsFetchedEvent");
  }
  
}
