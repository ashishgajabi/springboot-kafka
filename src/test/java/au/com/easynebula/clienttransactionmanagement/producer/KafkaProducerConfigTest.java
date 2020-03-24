package au.com.easynebula.clienttransactionmanagement.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class KafkaProducerConfigTest {

	private final String kafkaServerUrl = "kafkaServerUrl";

	private final KafkaProducerConfig producerConfig = new KafkaProducerConfig();

	@BeforeEach
	void setup() {
		setField(producerConfig, "kafkaServerUrl", kafkaServerUrl);
	}

	@Test
	void shouldHavePropertiesSetInProducerFactory() {
		final DefaultKafkaProducerFactory<String, String> producerFactory = (DefaultKafkaProducerFactory)producerConfig.producerFactory();

		assertNotNull(producerFactory);
		final Map<String, Object> properties = producerFactory.getConfigurationProperties();

		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.get(BOOTSTRAP_SERVERS_CONFIG), is(kafkaServerUrl));
		assertThat(properties.get(KEY_SERIALIZER_CLASS_CONFIG), is(StringSerializer.class));
		assertThat(properties.get(VALUE_SERIALIZER_CLASS_CONFIG), is(StringSerializer.class));
	}

	@Test
	void shouldProduceKafkaTemplate() {
		final KafkaTemplate<String, String> kafkaTemplate = producerConfig.kafkaTemplate();

		assertNotNull(kafkaTemplate);
		final DefaultKafkaProducerFactory<String, String> producerFactory = (DefaultKafkaProducerFactory)kafkaTemplate.getProducerFactory();

		assertNotNull(producerFactory);
		final Map<String, Object> properties = producerFactory.getConfigurationProperties();

		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.get(BOOTSTRAP_SERVERS_CONFIG), is(kafkaServerUrl));
		assertThat(properties.get(KEY_SERIALIZER_CLASS_CONFIG), is(StringSerializer.class));
		assertThat(properties.get(VALUE_SERIALIZER_CLASS_CONFIG), is(StringSerializer.class));
	}
}