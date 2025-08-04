package customers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab2PartAApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab2PartAApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ICustomerService customerService) {
		return args -> {
			customerService.addCustomer("Frank Brown", "fbrown@acme.com",
					"Main Street 5", "Chicago", "60613");
		};
	}
}
