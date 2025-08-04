package customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class CustomerDAOMock implements ICustomerDAO {

    @Autowired
    private ILogger logger;

    public void save(Customer customer) {
        System.out.println("CustomerDAOMock: pretending to save " + customer.getName());
        logger.log("MOCK: Saved customer " + customer.getName());
    }
}
