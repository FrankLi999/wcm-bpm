package com.bpwizard.wcm.repo.collector;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
// import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.wcm.repo.config.ModeshapeConfig;

@Configuration
// @EnableKafka
@AutoConfigureBefore({ModeshapeConfig.class, AutoConfiguration.class})
public class KafkaConsumerConfig implements KafkaListenerConfigurer {
	@Autowired
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	
	@Value("${bpw.wcm.kafka.bootstrap-servers}")
    private String bootstrapServers;

	@Value("${bpw.wcm.kafka.bootstrap-servers:wcm-event}")
    private String wcmEventGroup;
	
	@Autowired
	protected KafkaSecurityService kafkaSecurityService;

    Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, wcmEventGroup);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        return props;
    }

    ConsumerFactory<String, byte[]> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

	@Override
	public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
		// must keep this order
		registrar.setContainerFactory(kafkaListenerContainerFactory());
		MessageHandlerMethodFactory messageHandlerMethodFactory = new SpringSecurityAwareMessageHandlerFactory(kafkaSecurityService);
		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
	}
	
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, byte[]>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setMessageConverter(new ByteArrayJsonMessageConverter());
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate);
        // Comment the RecordFilterStrategy if Filtering is not required        
        // factory.setRecordFilterStrategy(record -> record.value().contains("ignored"));
        return factory;
    }
    
//    public ConsumerFactory<String, WcmEvent> wcmEventConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "reflectoring-user");
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(WcmEvent.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, WcmEvent> wcmEventKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, WcmEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(wcmEventConsumerFactory());
//        return factory;
//    }
//    
//    @Bean
//	public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaJsonListenerContainerFactory() {
//		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
//				new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		factory.setMessageConverter(new ByteArrayJsonMessageConverter());
//		return factory;
//	}
}
