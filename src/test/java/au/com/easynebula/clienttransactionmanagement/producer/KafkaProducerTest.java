package au.com.easynebula.clienttransactionmanagement.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	private final String topicName = "testTopicName";

	private KafkaProducer producer;

	@BeforeEach
	void setup() {
		producer = new KafkaProducer(kafkaTemplate);
		ReflectionTestUtils.setField(producer, "topic", topicName);
	}

	@Test
	void shouldSendMessageToGivenKafkaTopic() {
		final String message = "testMessage";
		producer.sendMessage(message);
		verify(kafkaTemplate).send(eq(topicName), eq(message));
	}
}