package app;

import domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("repositories")
@EntityScan("domain") 
public class OrderApplication implements CommandLineRunner{
	

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create different types of products
		Book book = new Book("B001", "Hibernate 3", 35.50, "978-1932394153");
		CD cd = new CD("C001", "The best of Queen", 12.98, "Queen");
		DVD dvd = new DVD("D001", "The Matrix", 19.99, "Sci-Fi");
		
		// Create order lines
		OrderLine ol1 = new OrderLine(2, book);
		OrderLine ol2 = new OrderLine(1, cd);
		OrderLine ol3 = new OrderLine(1, dvd);

		// Create order
		Order o1 = new Order("234743", "12/10/06", "open");
		o1.addOrderLine(ol1);
		o1.addOrderLine(ol2);
		o1.addOrderLine(ol3);

		// Create customer with address
		Customer c1 = new Customer("Frank", "Brown", "Mainstreet 1",
				"New York", "43221", "USA");
		c1.addOrder(o1);
		o1.setCustomer(c1);

		printOrder(o1);
	}

	public static void printOrder(Order order) {
		System.out.println("Order with orderNumber: " + order.getOrderNumber());
		System.out.println("Order date: " + order.getDate());
		System.out.println("Order status: " + order.getStatus());
		Customer cust = order.getCustomer();
		System.out.println("Customer: " + cust.getFname() + " "
				+ cust.getLastname());
		System.out.println("Address: " + cust.getAddress().getStreet() + ", " 
				+ cust.getAddress().getCity() + ", " 
				+ cust.getAddress().getZip() + ", " 
				+ cust.getAddress().getCountry());
		
		for (OrderLine orderline : order.getOrderlines()) {
			System.out.println("Order line: quantity= "
					+ orderline.getQuantity());
			Product product = orderline.getProduct();
			System.out.print("Product: " + product.getProductNumber() + " - " 
					+ product.getName() + " - $" + product.getPrice());
			
			// Print specific attributes based on product type
			if (product instanceof Book) {
				Book book = (Book) product;
				System.out.println(" (ISBN: " + book.getIsbn() + ")");
			} else if (product instanceof CD) {
				CD cd = (CD) product;
				System.out.println(" (Artist: " + cd.getArtist() + ")");
			} else if (product instanceof DVD) {
				DVD dvd = (DVD) product;
				System.out.println(" (Genre: " + dvd.getGenre() + ")");
			} else {
				System.out.println();
			}
		}
	}
}
