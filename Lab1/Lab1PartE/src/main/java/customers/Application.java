package customers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
	public static void main(String[] args) {
		// Load Spring context using Java-based configuration
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		// Get the service bean from the context
		ICustomerService customerService = context.getBean(ICustomerService.class);

		// Use the service
		customerService.addCustomer("Frank Brown", "fbrown@acme.com",
				"mainstreet 5", "Chicago", "60613");

		// NEW: Test ProductService
		ProductService productService = context.getBean(ProductService.class);
		productService.addProduct(new Product("Wireless Headphones"));
	}
}