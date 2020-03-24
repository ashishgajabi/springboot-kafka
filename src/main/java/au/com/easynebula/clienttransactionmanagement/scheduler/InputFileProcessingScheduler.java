package au.com.easynebula.clienttransactionmanagement.scheduler;

import au.com.easynebula.clienttransactionmanagement.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InputFileProcessingScheduler {
	private static final Logger LOGGER = LoggerFactory.getLogger(InputFileProcessingScheduler.class);

	private static final String FILE_NAME = "Input.txt";
	private static final String ARCHIVE_FOLDER = "archive";

	private final KafkaProducer producer;

	private final String filePath;

	@Autowired
	public InputFileProcessingScheduler(final KafkaProducer producer, @Value("${client-transaction.input.file-path}") final String filePath) {
		this.producer = producer;
		this.filePath = filePath;
	}

	@Scheduled(fixedDelay = 60000, initialDelay = 15000)
	public void clientTransactionFileReader() {
		final File file = new File(filePath + File.separator + FILE_NAME);
		final Path archivePath = Paths.get(filePath + File.separator + ARCHIVE_FOLDER);

		if (file.exists()) {
			LOGGER.info("Input file found. Processing started...");
			final int[] totalRecordsProcessed = {0};
			try (final BufferedReader br = new BufferedReader(new FileReader(file))) {
				br.lines().forEach(message -> {
						producer.sendMessage(message);
					totalRecordsProcessed[0]++;
				});
				Files.move(file.toPath(), archivePath.resolve(file.getName() + System.currentTimeMillis()));
			} catch (IOException e) {
				LOGGER.error("IOException while processing input file ", e);
			} catch (Exception e) {
				LOGGER.error("Exception while processing input file ", e);
			}
			LOGGER.info("Total number of Records processed: [{}]", totalRecordsProcessed[0]);
			LOGGER.info("Input file processing finished");
		} else {
			LOGGER.info("No new input file detected.");
		}
	}
}