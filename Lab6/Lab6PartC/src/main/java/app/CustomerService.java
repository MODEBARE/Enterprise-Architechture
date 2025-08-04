package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCustomersBatch(int count) {
        System.out.println("Inserting " + count + " customers using batch operations...");
        
        int batchSize = 1000;
        for (int i = 0; i < count; i++) {
            Customer customer = new Customer("John Doe " + i);
            Account account = new Account("123" + i);
            customer.setAccount(account);
            
            entityManager.persist(customer);
            
            // Flush and clear every batch to manage memory
            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
                System.out.println("Batched " + i + " customers");
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional(readOnly = true)
    public List<Customer> retrieveAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public void updateAllCustomersBatch(String newName) {
        System.out.println("Updating all customers using batch operations...");
        
        // Use JPQL for batch update - much more efficient
        int updatedCount = customerRepository.updateAllCustomerNames(newName);
        System.out.println("Updated " + updatedCount + " customers in batch");
    }

    @Transactional
    public void updateAllCustomersOldWay(String newName) {
        System.out.println("Updating customers the old way (individual saves)...");
        
        List<Customer> customers = customerRepository.findAll();
        int batchSize = 1000;
        int count = 0;
        
        for (Customer customer : customers) {
            customer.setName(newName);
            entityManager.merge(customer);
            
            count++;
            if (count % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    public long getCustomerCount() {
        return customerRepository.count();
    }
} 