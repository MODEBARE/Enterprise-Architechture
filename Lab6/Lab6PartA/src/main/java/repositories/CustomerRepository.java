package repositories;

import domain.Customer;
import domain.DVD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    
    // ======= METHOD NAME QUERIES =======
    // Query 1: Give all customers
    List<Customer> findAll();
    
    // Query 3: Give all customers with zip code 2389HI
    List<Customer> findByAddressZip(String zip);
    
    // Query 4: Give all customers who ordered a DVD with the name Rocky3
    List<Customer> findDistinctByTheOrdersOrderlinesProductName(String productName);
    
    // Alternative approach using @Query annotation for more complex DVD-specific query
    @Query("SELECT DISTINCT c FROM Customer c " +
           "JOIN c.theOrders o " +
           "JOIN o.orderlines ol " +
           "JOIN ol.product p " +
           "WHERE TYPE(p) = DVD AND p.name = :productName")
    List<Customer> findCustomersWhoOrderedDVDByName(@Param("productName") String productName);
    
    // Additional useful method names for customers
    List<Customer> findByFname(String fname);
    List<Customer> findByLastname(String lastname);
    List<Customer> findByFnameAndLastname(String fname, String lastname);
    
    // ======= NAMED QUERIES (defined in entity) =======
    // Named query: Give all customers from the USA
    @Query("SELECT c FROM Customer c WHERE c.address.country = 'USA'")
    List<Customer> findCustomersFromUSA();
    
    // ======= JPQL QUERIES WITH @Query =======
    // JPQL: Give the first and lastnames of all customers who live in Amsterdam
    @Query("SELECT c.fname, c.lastname FROM Customer c WHERE c.address.city = 'Amsterdam'")
    List<Object[]> findCustomerNamesInAmsterdam();
    
    // JPQL: Give all customers who live in Amsterdam (full objects)
    @Query("SELECT c FROM Customer c WHERE c.address.city = 'Amsterdam'")
    List<Customer> findCustomersInAmsterdam();
    
    // ======= NATIVE QUERIES =======
    // Native query: Give all customers who live in Amsterdam
    @Query(value = "SELECT c.* FROM customer c " +
                  "INNER JOIN address a ON c.address_id = a.id " +
                  "WHERE a.city = 'Amsterdam'", 
           nativeQuery = true)
    List<Customer> findCustomersInAmsterdamNative();
} 