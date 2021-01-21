package com.bpwizard.wcm.repo.collector;

import java.lang.reflect.Method;

import org.springframework.messaging.Message;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolverComposite;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

// org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory
public class SpringSecurityAwareMessageHandlerFactory extends DefaultMessageHandlerMethodFactory {
	
	protected KafkaSecurityService kafkaSecurityService;
	
	public SpringSecurityAwareMessageHandlerFactory(KafkaSecurityService kafkaSecurityService) {
		super();
		this.kafkaSecurityService = kafkaSecurityService;
		this.setMessageConverter(new ByteArrayMessageConverter());
	}
	
//	@Override
//	public InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method) {
//		InvocableHandlerMethod handlerMethod = new InvocableHandlerMethod(bean, method);
//		handlerMethod.setMessageMethodArgumentResolvers(this.argumentResolvers);
//		return handlerMethod;
//	}

	
	@Override
	public InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method) {
		
	    InvocableHandlerMethod m = new InvocableHandlerMethod(bean, method) {
	        @Override 
	        public Object invoke(Message<?> message, Object... providedArgs) throws Exception {
	            try {
	            	Object token = message.getHeaders().get("token");
	            	if (token != null) {
	            		kafkaSecurityService.setAuthenticationContext((String)token);
	            	}
	            	return super.invoke(message, providedArgs);
	            } finally {
	            	kafkaSecurityService.cleabAuthenticationContext();
	            }
	        }
	    };

	    HandlerMethodArgumentResolverComposite handlers = new HandlerMethodArgumentResolverComposite();
	    handlers.addResolvers(initArgumentResolvers());
	    m.setMessageMethodArgumentResolvers(handlers);
	    return m;
	}
}