package au.com.easynebula.clienttransactionmanagement.utils;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.time.LocalDate.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FixedWidthFileParserTest {

	private final FixedWidthFileParser<FutureTransaction> fixedWidthFileParser = new FixedWidthFileParser<>(FutureTransaction.class, 73);

	@Test
	void shouldAbleToSuccessfullyParseFixedWidthLine() {
		final String fixedWidthLne = "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O";

		final Optional<FutureTransaction> futureTransactionOptional = fixedWidthFileParser.parseLine(fixedWidthLne, FutureTransaction.class);
		final FutureTransaction futureTransaction = futureTransactionOptional.get();
		assertNotNull(futureTransaction);
		assertThat(futureTransaction.getClientType(), is("CL"));
		assertThat(futureTransaction.getClientNumber(), is("4321"));
		assertThat(futureTransaction.getAccountNumber(), is("0002"));
		assertThat(futureTransaction.getSubAccountNumber(), is("0001"));
		assertThat(futureTransaction.getProductGroupCode(), is("FU"));
		assertThat(futureTransaction.getExchangeCode(), is("SGX"));
		assertThat(futureTransaction.getSymbol(), is("NK"));
		assertThat(futureTransaction.getExpirationDate(), is(of(2010, 9, 10)));
		assertThat(futureTransaction.getQuantityLong(), is("0000000001"));
		assertThat(futureTransaction.getQuantityShort(), is("0000000000"));
		assertThat(futureTransaction.getTransactionDate(), is(of(2010, 8, 20)));
	}

	@Test
	void shouldAbleToHandleEmptyFixedWidthLine() {
		final String emptyLine = "";

		final Optional<FutureTransaction> futureTransactionOptional = fixedWidthFileParser.parseLine(emptyLine, FutureTransaction.class);
		assertFalse(futureTransactionOptional.isPresent());
	}

	@Test
	void shouldAbleToHandleNullFixedWidthLine() {
		final String nullLine = null;

		final Optional<FutureTransaction> futureTransactionOptional = fixedWidthFileParser.parseLine(nullLine, FutureTransaction.class);
		assertFalse(futureTransactionOptional.isPresent());
	}

	@Test
	void shouldAbleToHandleIncorrectLengthFixedWidthLine() {
		new FixedWidthFileParser<>(FutureTransaction.class, 4);
		final String longerLine = "123456";

		final Optional<FutureTransaction> futureTransactionOptional = fixedWidthFileParser.parseLine(longerLine, FutureTransaction.class);
		assertFalse(futureTransactionOptional.isPresent());
	}

	@Test
	void shouldAbleToHandleExceptionsWhileParsingFixedWidthLine() {
		class TestClass {
			public TestClass(int a) {

			}
		}
		FixedWidthFileParser<TestClass> parser = new FixedWidthFileParser<>(TestClass.class, 4);
		final String line = "123456";

		final Exception exception = assertThrows(IllegalArgumentException.class, () -> {parser.parseLine(line, TestClass.class);});
		assertThat(exception.getMessage(), containsString("Error parsing line: 123456"));
	}
}