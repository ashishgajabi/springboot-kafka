package au.com.easynebula.clienttransactionmanagement.consumer;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class KafkaConsumerConfig {

	private static final Logger LOG = getLogger(KafkaConsumerConfig.class);

	@Value("${kafka.server.url}")
	private String kafkaServerUrl;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		final Map<String, Object> props = new HashMap<>();
		props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
		props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		return getContainerFactory();
	}

	private ConcurrentKafkaListenerContainerFactory<String, String> getContainerFactory() {
		final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setAutoStartup(true); // this can be set to false and put behind listener startup lifecycle.
		return factory;
	}
}
