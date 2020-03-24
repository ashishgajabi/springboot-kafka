package au.com.easynebula.clienttransactionmanagement.repository;

import au.com.easynebula.clienttransactionmanagement.domain.FutureTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FutureTransactionRepository extends JpaRepository<FutureTransaction, String> {

	List<FutureTransaction> findByTransactionDate(LocalDate transactionDate);

}
