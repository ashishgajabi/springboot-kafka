package au.com.easynebula.clienttransactionmanagement.service;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;

import java.util.List;

public interface DailySummaryReportService {
	List<FutureTransaction> dailyFutureTransactions();
}
