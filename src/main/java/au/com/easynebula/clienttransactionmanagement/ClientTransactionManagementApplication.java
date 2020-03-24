package au.com.easynebula.clienttransactionmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClientTransactionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientTransactionManagementApplication.class, args);
	}
}
