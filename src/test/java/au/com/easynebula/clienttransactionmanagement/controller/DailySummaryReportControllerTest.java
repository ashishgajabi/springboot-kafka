package au.com.easynebula.clienttransactionmanagement.controller;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import au.com.easynebula.clienttransactionmanagement.service.DailySummaryReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static au.com.easynebula.clienttransactionmanagement.controller.DailySummaryReportController.HEADERS;
import static java.lang.String.format;
import static java.time.LocalDate.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@ExtendWith(MockitoExtension.class)
class DailySummaryReportControllerTest {

	@Mock
	private DailySummaryReportService service;

	private DailySummaryReportController controller;

	@BeforeEach
	void setup() {
		controller = new DailySummaryReportController(service);
	}

	@Test
	void shouldSuccessfullyGenerateDailyTransactionReport() throws UnsupportedEncodingException {
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String fileName = format("daily-summary-report-%s.%s", now(), ".csv");
		final String expectedRecord = format("ABCD,EFG%s,H-I", now());

		final FutureTransaction futureTransaction = new FutureTransaction();
		futureTransaction.setClientType("A");
		futureTransaction.setClientNumber("B");
		futureTransaction.setAccountNumber("C");
		futureTransaction.setSubAccountNumber("D");
		futureTransaction.setExchangeCode("E");
		futureTransaction.setProductGroupCode("F");
		futureTransaction.setSymbol("G");
		futureTransaction.setExpirationDate(now());
		futureTransaction.setQuantityLong("H");
		futureTransaction.setQuantityShort("I");

		given(service.dailyFutureTransactions()).willReturn(Arrays.asList(futureTransaction));

		controller.generateDailyReport(response);

		assertThat(response.getHeader(CONTENT_TYPE), is("text/csv"));
		assertThat(response.getHeader(CONTENT_DISPOSITION), is("attachment;filename="+fileName));
		assertThat(response.getContentAsString(), startsWith(String.join(",", HEADERS)));
		assertThat(response.getContentAsString().split("\n").length, is(2));
		assertThat(response.getContentAsString().split("\n")[1], startsWith(expectedRecord));
	}
}