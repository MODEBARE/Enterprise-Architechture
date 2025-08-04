package app;

import domain.*;
import repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "domain")
public class QueryTestApplication implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CDRepository cdRepository;
    
    @Autowired
    private DVDRepository dvdRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(QueryTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Create test data
        createTestData();
        
        System.out.println("=== Lab 5 Part A - Query Method Names Demo ===\n");
        
        // Query 1: Give all customers
        System.out.println("1. Give all customers:");
        List<Customer> allCustomers = customerRepository.findAll();
        allCustomers.forEach(customer -> 
            System.out.println("   - " + customer.getFname() + " " + customer.getLastname()));
        System.out.println();
        
        // Query 2: Give all CD's from U2 with a price smaller than 10 euro
        System.out.println("2. Give all CD's from U2 with a price smaller than 10 euro:");
        List<CD> u2CDs = cdRepository.findByArtistAndPriceLessThan("U2", 10.0);
        u2CDs.forEach(cd -> 
            System.out.println("   - " + cd.getName() + " by " + cd.getArtist() + " - $" + cd.getPrice()));
        System.out.println();
        
        // Query 3: Give all customers with zip code 2389HI
        System.out.println("3. Give all customers with zip code 2389HI:");
        List<Customer> customersByZip = customerRepository.findByAddressZip("2389HI");
        customersByZip.forEach(customer -> 
            System.out.println("   - " + customer.getFname() + " " + customer.getLastname() + 
                             " (Zip: " + customer.getAddress().getZip() + ")"));
        System.out.println();
        
        // Query 4: Give all customers who ordered a DVD with the name Rocky3
        System.out.println("4. Give all customers who ordered a DVD with the name Rocky3:");
        
        // Using method name approach
        List<Customer> customersByDVDName = customerRepository.findDistinctByTheOrdersOrderlinesProductName("Rocky3");
        System.out.println("   Method name approach:");
        customersByDVDName.forEach(customer -> 
            System.out.println("   - " + customer.getFname() + " " + customer.getLastname()));
            
        // Using @Query annotation approach (more precise for DVD type)
        List<Customer> customersByDVD = customerRepository.findCustomersWhoOrderedDVDByName("Rocky3");
        System.out.println("   @Query annotation approach (DVD-specific):");
        customersByDVD.forEach(customer -> 
            System.out.println("   - " + customer.getFname() + " " + customer.getLastname()));
        
        System.out.println("\n=== Repository Method Names Summary ===");
        System.out.println("1. customerRepository.findAll()");
        System.out.println("2. cdRepository.findByArtistAndPriceLessThan(\"U2\", 10.0)");
        System.out.println("3. customerRepository.findByAddressZip(\"2389HI\")");
        System.out.println("4a. customerRepository.findDistinctByTheOrdersOrderlinesProductName(\"Rocky3\")");
        System.out.println("4b. customerRepository.findCustomersWhoOrderedDVDByName(\"Rocky3\")");
    }
    
    private void createTestData() {
        // Create customers with addresses
        Customer customer1 = new Customer("John", "Doe", "Main St 123", "Amsterdam", "2389HI", "Netherlands");
        Customer customer2 = new Customer("Jane", "Smith", "Oak Ave 456", "Utrecht", "1234AB", "Netherlands");
        Customer customer3 = new Customer("Bob", "Johnson", "Pine Rd 789", "Rotterdam", "2389HI", "Netherlands");
        
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        
        // Create products
        CD u2CD1 = new CD("CD001", "The Joshua Tree", 8.99, "U2");
        CD u2CD2 = new CD("CD002", "Achtung Baby", 12.50, "U2");
        CD otherCD = new CD("CD003", "Abbey Road", 15.99, "The Beatles");
        
        DVD rocky3DVD = new DVD("DVD001", "Rocky3", 9.99, "Action");
        DVD actionDVD = new DVD("DVD002", "Die Hard", 14.99, "Action");
        
        cdRepository.save(u2CD1);
        cdRepository.save(u2CD2);
        cdRepository.save(otherCD);
        dvdRepository.save(rocky3DVD);
        dvdRepository.save(actionDVD);
        
        // Create orders and orderlines
        Order order1 = new Order("ORD001", "2023-01-15", "completed");
        order1.setCustomer(customer1);
        OrderLine ol1 = new OrderLine(1, u2CD1);
        OrderLine ol2 = new OrderLine(1, rocky3DVD);
        order1.addOrderLine(ol1);
        order1.addOrderLine(ol2);
        customer1.addOrder(order1);
        
        Order order2 = new Order("ORD002", "2023-01-20", "completed");
        order2.setCustomer(customer3);
        OrderLine ol3 = new OrderLine(2, u2CD2);
        order2.addOrderLine(ol3);
        customer3.addOrder(order2);
        
        customerRepository.save(customer1);
        customerRepository.save(customer3);
    }
} 