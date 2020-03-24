package au.com.easynebula.clienttransactionmanagement.consumer;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigTest {

	private final String kafkaServerUrl = "kafkaServerUrl";

	private final KafkaConsumerConfig consumerConfig = new KafkaConsumerConfig();

	@BeforeEach
	void setup() {
		setField(consumerConfig, "kafkaServerUrl", kafkaServerUrl);
	}

	@Test
	public void shouldHavePropertiesSetInConsumerFactory() {
		final ConsumerFactory<String, String> consumerFactory = consumerConfig.consumerFactory();
		final Map<String, Object> properties = consumerFactory.getConfigurationProperties();

		assertNotNull(consumerFactory);
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.get(BOOTSTRAP_SERVERS_CONFIG), is(kafkaServerUrl));
		assertThat(properties.get(KEY_DESERIALIZER_CLASS_CONFIG), is(StringDeserializer.class));
		assertThat(properties.get(VALUE_DESERIALIZER_CLASS_CONFIG), is(StringDeserializer.class));
	}

	@Test
	public void shouldCreateAlertConcurrentKafkaListenerContainerFactory() {
		final ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory = consumerConfig.kafkaListenerContainerFactory();

		assertNotNull(concurrentKafkaListenerContainerFactory);
		assertNotNull(concurrentKafkaListenerContainerFactory.getConsumerFactory());
	}
}