package au.com.easynebula.clienttransactionmanagement.controller;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import au.com.easynebula.clienttransactionmanagement.service.DailySummaryReportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Controller
public class DailySummaryReportController {

	private static final Logger LOG = LoggerFactory.getLogger(DailySummaryReportController.class);

	private final DailySummaryReportService service;

	public static final String[] HEADERS = { "Client_Information", "Product_Information", "Total_Transaction_Amount"};

	@Autowired
	public DailySummaryReportController(final DailySummaryReportService service) {
		this.service = service;
	}

	@GetMapping("/dailyReport")
	public void generateDailyReport(final HttpServletResponse response) {
		LOG.info("received request to download daily transaction report");

		final String fileName = format("daily-summary-report-%s.%s", LocalDate.now(), ".csv");
		response.addHeader(CONTENT_DISPOSITION, format("attachment;filename=%s", fileName));
		response.addHeader(CONTENT_TYPE, "text/csv");

		final List<FutureTransaction> futureTransactions = service.dailyFutureTransactions();
		LOG.info("found [{}] transactions to be exported to csv file", futureTransactions.size());
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			toCsv(writer, futureTransactions);
		} catch (final IOException e) {
			LOG.error("IO Error while downloading daily summary report", e);
		} finally {
			LOG.info("Finished generating csv file");
			if(writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	private void toCsv(final PrintWriter writer, final List<FutureTransaction> futureTransactions) throws IOException{
		LOG.info("Started generating csv file");
		try(CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			for (FutureTransaction ft : futureTransactions) {
				final String clientInfo = String.join("", ft.getClientType(), ft.getClientNumber(), ft.getAccountNumber(), ft.getSubAccountNumber());
				final String productInfo = String.join("", ft.getExchangeCode(), ft.getProductGroupCode(), ft.getSymbol(), ft.getExpirationDate().toString());
				final String transaction = String.join("", ft.getQuantityLong(), "-", ft.getQuantityShort());
				printer.printRecord(clientInfo, productInfo, transaction);
			}
		}
	}
}
