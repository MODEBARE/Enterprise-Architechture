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

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// ---- CUSTOMER + CREDIT CARD ----
		customerDao.clearDB();

		Customer customer1 = new Customer(101, "John Doe", "johnd@acme.com", "0622341678");
		CreditCard card1 = new CreditCard("12324564321", "Visa", "11/23");
		customer1.setCreditCard(card1);
		customerDao.save(customer1);

		Customer customer2 = new Customer(66, "James Johnson", "jj123@acme.com", "068633452");
		CreditCard card2 = new CreditCard("99876549876", "MasterCard", "01/24");
		customer2.setCreditCard(card2);
		customerDao.save(customer2);

		System.out.println("Customer 101: " + customerDao.getCustomer(101));
		System.out.println("Customer 66: " + customerDao.getCustomer(66));
		System.out.println("All customers:");
		System.out.println(customerDao.getAllCustomers());

		// ---- PRODUCT + SUPPLIER ----
		try (Connection conn = dataSource.getConnection()) {
			ProductDAO productDAO = new ProductDAOImpl(conn);

			// Create and save products with suppliers
			Product p1 = new Product(1, "Printer", 150.0);
			p1.setSupplier(new Supplier("Canon Inc", "123-456-7890"));

			Product p2 = new Product(2, "Scanner", 89.99);
			p2.setSupplier(new Supplier("Epson Tech", "987-654-3210"));

			Product p3 = new Product(3, "Paper", 19.99);
			p3.setSupplier(new Supplier("Office Depot", "111-222-3333"));

			productDAO.save(p1);
			productDAO.save(p2);
			productDAO.save(p3);

			System.out.println("\n--- Find product by ID 2 ---");
			System.out.println(productDAO.findByProductNumber(2));

			System.out.println("\n--- All products ---");
			productDAO.getAllProducts().forEach(System.out::println);

			System.out.println("\n--- Products by name 'Printer' ---");
			productDAO.findByProductName("Printer").forEach(System.out::println);

			System.out.println("\n--- Removing product 1 (Printer) ---");
			productDAO.removeProduct(1);

			System.out.println("\n--- All products after deletion ---");
			productDAO.getAllProducts().forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
