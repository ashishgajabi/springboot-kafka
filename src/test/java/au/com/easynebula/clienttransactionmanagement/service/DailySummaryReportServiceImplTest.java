package au.com.easynebula.clienttransactionmanagement.service;

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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DailySummaryReportServiceImplTest {

	@Mock
	private FutureTransactionRepository repository;

	@Captor
	private ArgumentCaptor<LocalDate> captor;

	private DailySummaryReportService service;

	@BeforeEach
	void setup() {
		service = new DailySummaryReportServiceImpl(repository);
	}

	@Test
	void shouldRetreiveFutureTransactionsFromRepo() {
		final LocalDate todayDate = LocalDate.now();
		service.dailyFutureTransactions();
		verify(repository).findByTransactionDate(captor.capture());

		final LocalDate actualValue = captor.getValue();

		assertThat(actualValue, is(todayDate));
	}
}