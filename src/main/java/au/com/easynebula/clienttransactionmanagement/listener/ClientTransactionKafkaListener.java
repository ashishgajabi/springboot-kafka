package au.com.easynebula.clienttransactionmanagement.listener;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import au.com.easynebula.clienttransactionmanagement.repository.FutureTransactionRepository;
import au.com.easynebula.clienttransactionmanagement.utils.FixedWidthFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientTransactionKafkaListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientTransactionKafkaListener.class);

	private final FutureTransactionRepository repository;
	private final FixedWidthFileParser<FutureTransaction> fixedWidthFileParser;

	@Autowired
	public ClientTransactionKafkaListener(final FutureTransactionRepository repository, @Value("${client-transaction.message.min.length:0}") final int transactionMessageMinLength) {
		LOGGER.info("listener created");
		this.repository = repository;
		this.fixedWidthFileParser = new FixedWidthFileParser<>(FutureTransaction.class, transactionMessageMinLength);
	}

	@KafkaListener(id = "${kafka.consumer.group}", topics = "${kafka.topic.name}",
			containerFactory = "kafkaListenerContainerFactory", groupId = "${kafka.consumer.group}")
	public void listen(@Payload final String message) {
		LOGGER.info("received message on kafka listener");
		processMessage(message);
	}

	private void processMessage(final String message) {
		try {
			final Optional<FutureTransaction> optionalFutureTransaction = fixedWidthFileParser.parseLine(message, FutureTransaction.class);
			optionalFutureTransaction.ifPresent(futureTransaction -> {
				LOGGER.debug("About to store future transaction {}", futureTransaction);
				repository.save(futureTransaction);
				LOGGER.info("successfully processed received message.");
			});
		} catch (Exception e) {
			LOGGER.error("Exception while parsing message or saving to database", e);
		}
	}
}