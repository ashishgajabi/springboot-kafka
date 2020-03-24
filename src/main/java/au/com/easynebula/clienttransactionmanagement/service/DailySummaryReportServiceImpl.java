package au.com.easynebula.clienttransactionmanagement.service;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import au.com.easynebula.clienttransactionmanagement.repository.FutureTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDate.now;

@Service
public class DailySummaryReportServiceImpl implements DailySummaryReportService {

	private static final Logger LOG = LoggerFactory.getLogger(DailySummaryReportServiceImpl.class);

	private final FutureTransactionRepository repository;

	@Autowired
	public DailySummaryReportServiceImpl(final FutureTransactionRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<FutureTransaction> dailyFutureTransactions() {
		LOG.info("retrieving all transactions for todays' date {}", now());
		return repository.findByTransactionDate(now());
	}
}
