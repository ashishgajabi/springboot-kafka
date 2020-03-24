package au.com.easynebula.clienttransactionmanagement.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

	@Value("${kafka.topic.name:testTopic}")
	private String topic;

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public KafkaProducer(final KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(final String message) {
		kafkaTemplate.send(topic, message);
	}
}