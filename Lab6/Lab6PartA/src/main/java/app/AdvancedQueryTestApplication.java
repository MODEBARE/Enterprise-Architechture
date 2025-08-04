package app;

import domain.*;
import repositories.*;
import specifications.*;
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
public class AdvancedQueryTestApplication implements CommandLineRunner {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private CDRepository cdRepository;
    @Autowired private DVDRepository dvdRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private AddressRepository addressRepository;

    public static void main(String[] args) {
        SpringApplication.run(AdvancedQueryTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Create comprehensive test data
        createAdvancedTestData();
        
        System.out.println("=== Lab 5 Part A - All Query Types Demo ===\n");
        
        testNamedQueries();
        testJPQLQueries();
        testNativeQueries();
        testSpecifications();
        
        System.out.println("\n=== Summary of All Query Types ===");
        printQuerySummary();
    }
    
    private void testNamedQueries() {
        System.out.println("========= NAMED QUERIES =========");
        
        // Named Query: Give all customers from the USA
        System.out.println("1. Named Query - All customers from USA:");
        List<Customer> usaCustomers = customerRepository.findCustomersFromUSA();
        usaCustomers.forEach(customer -> 
            System.out.println("   - " + customer.getFname() + " " + customer.getLastname() + 
                             " (" + customer.getAddress().getCountry() + ")"));
        
        // Named Query: Give all CD's from a certain artist
        System.out.println("\n2. Named Query - All CDs from U2:");
        List<CD> u2CDs = cdRepository.findCDsByArtistNamed("U2");
        u2CDs.forEach(cd -> 
            System.out.println("   - " + cd.getName() + " by " + cd.getArtist() + " - $" + cd.getPrice()));
        
        System.out.println();
    }
    
    private void testJPQLQueries() {
        System.out.println("========= JPQL QUERIES WITH @Query =========");
        
        // JPQL: Give the ordernumbers of all orders with status 'closed'
        System.out.println("1. JPQL - Order numbers with status 'closed':");
        List<String> closedOrderNumbers = orderRepository.findOrderNumbersByStatusClosed();
        closedOrderNumbers.forEach(orderNumber -> 
            System.out.println("   - Order #" + orderNumber));
        
        // JPQL: Give the first and lastnames of all customers who live in Amsterdam
        System.out.println("\n2. JPQL - Customer names in Amsterdam:");
        List<Object[]> customerNames = customerRepository.findCustomerNamesInAmsterdam();
        customerNames.forEach(names -> 
            System.out.println("   - " + names[0] + " " + names[1]));
        
        // JPQL: Give the ordernumbers of all orders from customers who live in a certain city
        System.out.println("\n3. JPQL - Order numbers from customers in Amsterdam:");
        List<String> amsterdamOrders = orderRepository.findOrderNumbersByCustomerCity("Amsterdam");
        amsterdamOrders.forEach(orderNumber -> 
            System.out.println("   - Order #" + orderNumber));
        
        // JPQL: Give all CD's from a certain artist with a price bigger than a certain amount
        System.out.println("\n4. JPQL - U2 CDs with price > $10:");
        List<CD> expensiveU2CDs = cdRepository.findCDsByArtistAndPriceGreaterThan("U2", 10.0);
        expensiveU2CDs.forEach(cd -> 
            System.out.println("   - " + cd.getName() + " by " + cd.getArtist() + " - $" + cd.getPrice()));
        
        System.out.println();
    }
    
    private void testNativeQueries() {
        System.out.println("========= NATIVE QUERIES =========");
        
        // Native Query: Give all addresses in Amsterdam
        System.out.println("1. Native Query - All addresses in Amsterdam:");
        List<Address> amsterdamAddresses = addressRepository.findAddressesInAmsterdamNative();
        amsterdamAddresses.forEach(address -> 
            System.out.println("   - " + address.getStreet() + ", " + address.getCity() + 
                             ", " + address.getZip() + ", " + address.getCountry()));
        
        // Native Query: Give all CD's from U2
        System.out.println("\n2. Native Query - All U2 CDs:");
        List<Object[]> u2CDsNative = cdRepository.findU2CDsNative();
        u2CDsNative.forEach(cd -> {
            // Object[] contains: id, name, price, product_number, artist
            // HSQLDB returns BigInteger for IDENTITY columns
            Number idNumber = (Number) cd[0];
            Long id = idNumber.longValue();
            String name = (String) cd[1];
            Double price = (Double) cd[2];
            String productNumber = (String) cd[3];
            String artist = (String) cd[4];
            System.out.println("   - " + name + " by " + artist + " - $" + price + " (ID: " + id + ")");
        });
        
        System.out.println();
    }
    
    private void testSpecifications() {
        System.out.println("========= SPECIFICATIONS (JPA Criteria API) =========");
        
        // Specification: Give the ordernumbers of all orders with status 'closed'
        System.out.println("1. Specification - Orders with status 'closed':");
        List<Order> closedOrders = orderRepository.findAll(OrderSpecifications.hasStatusClosed());
        closedOrders.forEach(order -> 
            System.out.println("   - Order #" + order.getOrderNumber() + " (Status: " + order.getStatus() + ")"));
        
        // Specification: Give all customers who live in Amsterdam
        System.out.println("\n2. Specification - Customers in Amsterdam:");
        List<Customer> amsterdamCustomers = customerRepository.findAll(CustomerSpecifications.livesInAmsterdam());
        amsterdamCustomers.forEach(customer -> 
            System.out.println("   - " + customer.getFname() + " " + customer.getLastname() + 
                             " (" + customer.getAddress().getCity() + ")"));
        
        // Specification: Give all CD's from a certain artist with a price bigger than a certain amount
        System.out.println("\n3. Specification - U2 CDs with price > $10:");
        List<CD> expensiveU2CDsSpec = cdRepository.findAll(
            CDSpecifications.hasArtistAndPriceGreaterThan("U2", 10.0));
        expensiveU2CDsSpec.forEach(cd -> 
            System.out.println("   - " + cd.getName() + " by " + cd.getArtist() + " - $" + cd.getPrice()));
        
        // Specification: Combined criteria using Specification.and()
        System.out.println("\n4. Specification - Combined criteria (CDs from U2 AND price < $15):");
        List<CD> combinedCriteria = cdRepository.findAll(
            CDSpecifications.byArtist("U2").and(CDSpecifications.priceLessThan(15.0)));
        combinedCriteria.forEach(cd -> 
            System.out.println("   - " + cd.getName() + " by " + cd.getArtist() + " - $" + cd.getPrice()));
        
        System.out.println();
    }
    
    private void printQuerySummary() {
        System.out.println("NAMED QUERIES:");
        System.out.println("  • customerRepository.findCustomersFromUSA()");
        System.out.println("  • cdRepository.findCDsByArtistNamed(artist)");
        
        System.out.println("\nJPQL QUERIES (@Query):");
        System.out.println("  • orderRepository.findOrderNumbersByStatusClosed()");
        System.out.println("  • customerRepository.findCustomerNamesInAmsterdam()");
        System.out.println("  • orderRepository.findOrderNumbersByCustomerCity(city)");
        System.out.println("  • cdRepository.findCDsByArtistAndPriceGreaterThan(artist, amount)");
        
        System.out.println("\nNATIVE QUERIES:");
        System.out.println("  • addressRepository.findAddressesInAmsterdamNative()");
        System.out.println("  • cdRepository.findU2CDsNative()");
        
        System.out.println("\nSPECIFICATIONS:");
        System.out.println("  • orderRepository.findAll(OrderSpecifications.hasStatusClosed())");
        System.out.println("  • customerRepository.findAll(CustomerSpecifications.livesInAmsterdam())");
        System.out.println("  • cdRepository.findAll(CDSpecifications.hasArtistAndPriceGreaterThan(artist, amount))");
        System.out.println("  • Combined: CDSpecifications.byArtist().and(CDSpecifications.priceLessThan())");
    }
    
    private void createAdvancedTestData() {
        // Create customers from different countries
        Customer customer1 = new Customer("John", "Doe", "Canal St 123", "Amsterdam", "1012AB", "Netherlands");
        Customer customer2 = new Customer("Jane", "Smith", "Broadway 456", "New York", "10001", "USA");
        Customer customer3 = new Customer("Bob", "Johnson", "Damrak 789", "Amsterdam", "1012CD", "Netherlands");
        Customer customer4 = new Customer("Alice", "Brown", "Fifth Ave 321", "New York", "10002", "USA");
        
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
        
        // Create variety of CDs with different prices
        CD u2CD1 = new CD("CD001", "The Joshua Tree", 8.99, "U2");
        CD u2CD2 = new CD("CD002", "Achtung Baby", 12.50, "U2");
        CD u2CD3 = new CD("CD003", "How to Dismantle an Atomic Bomb", 14.99, "U2");
        CD beatlesCD = new CD("CD004", "Abbey Road", 15.99, "The Beatles");
        CD pinkFloydCD = new CD("CD005", "Dark Side of the Moon", 13.99, "Pink Floyd");
        
        cdRepository.save(u2CD1);
        cdRepository.save(u2CD2);
        cdRepository.save(u2CD3);
        cdRepository.save(beatlesCD);
        cdRepository.save(pinkFloydCD);
        
        // Create DVDs
        DVD actionDVD = new DVD("DVD001", "Die Hard", 14.99, "Action");
        DVD comedyDVD = new DVD("DVD002", "The Hangover", 12.99, "Comedy");
        
        dvdRepository.save(actionDVD);
        dvdRepository.save(comedyDVD);
        
        // Create orders with different statuses
        Order order1 = new Order("ORD001", "2023-01-15", "closed");
        order1.setCustomer(customer1); // Amsterdam customer
        OrderLine ol1 = new OrderLine(1, u2CD1);
        order1.addOrderLine(ol1);
        customer1.addOrder(order1);
        
        Order order2 = new Order("ORD002", "2023-01-20", "open");
        order2.setCustomer(customer2); // USA customer
        OrderLine ol2 = new OrderLine(1, u2CD2);
        order2.addOrderLine(ol2);
        customer2.addOrder(order2);
        
        Order order3 = new Order("ORD003", "2023-01-25", "closed");
        order3.setCustomer(customer3); // Amsterdam customer
        OrderLine ol3 = new OrderLine(1, beatlesCD);
        order3.addOrderLine(ol3);
        customer3.addOrder(order3);
        
        Order order4 = new Order("ORD004", "2023-01-30", "shipped");
        order4.setCustomer(customer4); // USA customer
        OrderLine ol4 = new OrderLine(1, u2CD3);
        order4.addOrderLine(ol4);
        customer4.addOrder(order4);
        
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
    }
} 