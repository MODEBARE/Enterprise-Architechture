package lab8.BankClientApp;

//package lab8.bankclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@SpringBootApplication
public class BankClientAppApplication implements CommandLineRunner {

	private final String BASE_URL = "http://localhost:8080/api/bank";
	private final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(BankClientAppApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Create Account
		BankAccount acc1 = new BankAccount("1001", "Elijah", 100.00);
		BankAccount acc2 = new BankAccount("1002", "Sarah", 50.00);

		restTemplate.postForObject(BASE_URL + "/create", acc1, String.class);
		restTemplate.postForObject(BASE_URL + "/create", acc2, String.class);

		// Deposit
		restTemplate.postForObject(BASE_URL + "/deposit?accountNumber=1001&amount=200", null, String.class);

		// Withdraw (valid)
		try {
			restTemplate.postForObject(BASE_URL + "/withdraw?accountNumber=1001&amount=50", null, String.class);
		} catch (Exception e) {
			System.out.println("Withdraw failed: " + e.getMessage());
		}

		// Withdraw (invalid)
		try {
			restTemplate.postForObject(BASE_URL + "/withdraw?accountNumber=1002&amount=1000", null, String.class);
		} catch (Exception e) {
			System.out.println("Withdraw failed (as expected): " + e.getMessage());
		}

		// Transfer (valid)
		try {
			restTemplate.postForObject(BASE_URL + "/transfer?from=1001&to=1002&amount=100", null, String.class);
		} catch (Exception e) {
			System.out.println("Transfer failed: " + e.getMessage());
		}

		// Transfer (invalid)
		try {
			restTemplate.postForObject(BASE_URL + "/transfer?from=1002&to=1001&amount=9999", null, String.class);
		} catch (Exception e) {
			System.out.println("Transfer failed (as expected): " + e.getMessage());
		}

		// Print account info
		BankAccount updated = restTemplate.getForObject(BASE_URL + "/1001", BankAccount.class);
		System.out.println("Final balance (1001): " + updated.getBalance());
	}
}

