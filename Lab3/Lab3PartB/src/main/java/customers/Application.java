package customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private CustomerDAO customerDao;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// --- JDBC: Customer Test ---
		//customerDao.clearDB();

		Customer customer1 = new Customer(101, "John Doe", "johnd@acme.com", "0622341678");
		customerDao.save(customer1);

		Customer customer2 = new Customer(66, "James Johnson", "jj123@acme.com", "068633452");
		customerDao.save(customer2);

		System.out.println("Customer 101: " + customerDao.getCustomer(101));
		System.out.println("Customer 66: " + customerDao.getCustomer(66));
		System.out.println("All Customers:");
		System.out.println(customerDao.getAllCustomers());

		// --- JPA: Book Entity Test ---
		System.out.println("\n--- JPA Book Operations ---");

		// Save books
		Book b1 = new Book("The Alchemist", "978-0061122415", "Paulo Coelho", 10.99);
		Book b2 = new Book("Clean Code", "978-0132350884", "Robert C. Martin", 45.00);
		Book b3 = new Book("Thinking in Java", "978-0131872486", "Bruce Eckel", 30.00);

		bookRepository.save(b1);
		bookRepository.save(b2);
		bookRepository.save(b3);

		System.out.println("\nAll books:");
		bookRepository.findAll().forEach(System.out::println);

		// Update a book
		b2.setPrice(39.99);
		bookRepository.save(b2);

		// Delete a book
		bookRepository.delete(b1);

		System.out.println("\nBooks after update and delete:");
		bookRepository.findAll().forEach(System.out::println);
	}
}
