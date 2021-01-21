package com.bpwizard.wcm.repo.syndicator;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.lang.Nullable;

import com.bpwizard.spring.boot.commons.service.AutoConfiguration;
import com.bpwizard.wcm.repo.config.ModeshapeConfig;

@Configuration
@EnableKafka
@AutoConfigureBefore({ModeshapeConfig.class, AutoConfiguration.class})
public class KafkaProducerConfig {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Value("${bpw.wcm.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}

	@Bean
	ProducerFactory<String, byte[]> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	KafkaTemplate<String, byte[]> kafkaTemplate() {
		KafkaTemplate<String, byte[]> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
		kafkaTemplate.setDefaultTopic("wcm-event");
		kafkaTemplate.setProducerListener(new ProducerListener<String, byte[]>() {
			@Override
			public void onSuccess(ProducerRecord<String, byte[]> producerRecord, RecordMetadata recordMetadata) {
				LOG.info("ACK from ProducerListener message: {} offset:  {}", producerRecord.value(),
						recordMetadata.offset());
			}
			
			public void onError(ProducerRecord<String, byte[]> producerRecord, @Nullable RecordMetadata recordMetadata,
					Exception exception) {
				
				LOG.error("Error ACK from ProducerListener message: {} offset: {}. Error: {}", producerRecord.value(),
						recordMetadata.offset(), exception);
			}
		});
		return kafkaTemplate;
	}

//	@Bean
//	public RoutingKafkaTemplate routingTemplate(GenericApplicationContext context) {
//
//		// ProducerFactory with Bytes serializer
//		Map<String, Object> props = new HashMap<>();
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
//		DefaultKafkaProducerFactory<Object, Object> bytesPF = new DefaultKafkaProducerFactory<>(props);
//		context.registerBean(DefaultKafkaProducerFactory.class, "bytesPF", bytesPF);
//
//		// ProducerFactory with String serializer
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		DefaultKafkaProducerFactory<Object, Object> stringPF = new DefaultKafkaProducerFactory<>(props);
//
//		Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
//		map.put(Pattern.compile(".*-bytes"), bytesPF);
//		map.put(Pattern.compile("wcm-event-.*"), stringPF);
//		return new RoutingKafkaTemplate(map);
//	}

//	@Bean
//	public ProducerFactory<String, WcmEvent> wcmEventProducerFactory() {
//		Map<String, Object> configProps = new HashMap<>();
//		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		return new DefaultKafkaProducerFactory<>(configProps);
//	}
//
//	@Bean
//	public KafkaTemplate<String, WcmEvent> wcmEventKafkaTemplate() {
//		return new KafkaTemplate<>(wcmEventProducerFactory());
//	}
}