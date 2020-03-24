package au.com.easynebula.clienttransactionmanagement.scheduler;

import au.com.easynebula.clienttransactionmanagement.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.getProperty;
import static java.nio.file.Files.createDirectory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class InputFileProcessingSchedulerTest {

	@Mock
	private KafkaProducer producer;

	private final String filePath = getProperty("java.io.tmpdir");

	private InputFileProcessingScheduler scheduler;

	@Captor
	private ArgumentCaptor<String> captor;

	@BeforeEach
	void setup() {
		scheduler = new InputFileProcessingScheduler(producer, filePath);

		try {
			deleteDirectory(new File(getProperty("java.io.tmpdir") + "/" + "archive"));
			createDirectory(Paths.get(getProperty("java.io.tmpdir") + "/" + "archive"));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	void shouldNeverSendMessageToKafkaIfFileDoesNotExists() {
		scheduler.clientTransactionFileReader();
		verifyNoInteractions(producer);
	}

	@Test
	void shouldReadFileAndSendMEssageToKafka() {
		final String fileContent = "sometext";
		try {
			Files.write(Paths.get(getProperty("java.io.tmpdir") +"/"+ "Input.txt"), fileContent.getBytes());
			scheduler.clientTransactionFileReader();
			verify(producer).sendMessage(captor.capture());
			final String message = captor.getValue();
			assertThat(message, is(fileContent));
			verifyNoMoreInteractions(producer);
		} catch (IOException e) {
			fail();
		}
	}

	private void deleteDirectory(final File directoryToBeDeleted) {
		if (directoryToBeDeleted.exists()) {
			File[] allContents = directoryToBeDeleted.listFiles();
			if (allContents != null) {
				for (File file : allContents) {
					deleteDirectory(file);
				}
			}
			directoryToBeDeleted.delete();
		}
	}
}