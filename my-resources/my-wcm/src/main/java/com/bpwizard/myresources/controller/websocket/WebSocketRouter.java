package com.bpwizard.myresources.controller.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

// @Configuration
public class WebSocketRouter {

//    @Autowired
//    private WebSocketHandler webSocketHandler;
//
//    @Bean
//    public HandlerMapping webSocketMapping() {
//        Map<String, WebSocketHandler> map = new HashMap<>();
//        map.put("/ws-one-per-second", webSocketHandler);
//
//        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
//        mapping.setOrder(10);
//        mapping.setUrlMap(map);
//        return mapping;
//    }
    
    // @Bean
    public HandlerMapping webSocketMapping(CommentService commentService,
						InboundChatService inboundChatService,
						OutboundChatService outboundChatService,
						MessageWebSocketHandler webSocketHandler) {
		Map<String, WebSocketHandler> urlMap = new HashMap<>();
		urlMap.put("/topic/comments.new", commentService);
		urlMap.put("/app/chatMessage.new", inboundChatService);
		urlMap.put("/topic/chatMessage.new", outboundChatService);
		urlMap.put("/ws-one-per-second", webSocketHandler);
		// tag::cors[]
//		
//		Map<String, CorsConfiguration> corsConfigurationMap =
//			new HashMap<>();
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.addAllowedOrigin("http://localhost:9010");
//		corsConfigurationMap.put("/ws-one-per-second", corsConfiguration);
//		corsConfigurationMap.put("/topic/comments.new", corsConfiguration);
//		corsConfigurationMap.put("/app/chatMessage.new", corsConfiguration);
//		corsConfigurationMap.put("/topic/chatMessage.new", corsConfiguration);
		// end::cors[]

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(10);
		mapping.setUrlMap(urlMap);
//		mapping.setCorsConfigurations(corsConfigurationMap);

		return mapping;
	}

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}