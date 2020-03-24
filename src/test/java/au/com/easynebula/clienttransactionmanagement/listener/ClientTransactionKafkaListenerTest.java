package au.com.easynebula.clienttransactionmanagement.listener;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import au.com.easynebula.clienttransactionmanagement.repository.FutureTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ClientTransactionKafkaListenerTest {

	@Mock
	private FutureTransactionRepository repository;

	@Captor
	private ArgumentCaptor<FutureTransaction> captor;

	private ClientTransactionKafkaListener listener;

	@BeforeEach
	void setup() {
		listener = new ClientTransactionKafkaListener(repository, 129);
	}

	@Test
	void shouldAbleToParseReceivedMessageAndSaveToDatabase() {
		final String message = "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O";

		listener.listen(message);

		verify(repository).save(captor.capture());
		final FutureTransaction value = captor.getValue();
		assertNotNull(value);
		assertThat(value.getClientType(), is("CL"));
		assertThat(value.getClientNumber(), is("4321"));
		assertThat(value.getAccountNumber(), is("0002"));
		assertThat(value.getExpirationDate(), is(LocalDate.of(2010, 9, 10)));
	}

	@Test
	void shouldAbleToHandleEmptyMessage() {
		final String blankMessage = "";

		listener.listen(blankMessage);

		verifyNoInteractions(repository);
	}

	@Test
	void shouldAbleToHandleNullMessage() {
		final String nullMessage = null;

		listener.listen(nullMessage);

		verifyNoInteractions(repository);
	}

	@Test
	void shouldNotParseMessageIfIncorrectLength() {
		listener = new ClientTransactionKafkaListener(repository, 5);
		final String messageWithLength6 = "123456";

		listener.listen(messageWithLength6);

		verifyNoInteractions(repository);
	}
}