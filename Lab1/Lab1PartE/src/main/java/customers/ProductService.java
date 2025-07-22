package customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductService {

    private final ILogger logger;
    private final IEmailSender emailSender;

    @Autowired
    public ProductService(ILogger logger, IEmailSender emailSender) {
        this.logger = logger;
        this.emailSender = emailSender;
    }

    public void addProduct(Product product) {
        // Simulate DB save
        logger.log("Saving product to database: " + product.getName());
        System.out.println("ProductService: saving product " + product.getName());

        // Send email
        String message = "New product added: " + product.getName();
        emailSender.sendEmail("admin@acme.com", message);
        logger.log("ProductService: Email sent for product " + product.getName());
    }
}
