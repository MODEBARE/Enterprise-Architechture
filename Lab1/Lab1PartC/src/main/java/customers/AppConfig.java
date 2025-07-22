package customers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ILogger logger() {
        return new Logger();
    }

    @Bean
    public ICustomerDAO customerDAO() {
        return new CustomerDAO(logger());
    }

    @Bean
    public IEmailSender emailSender() {
        return new EmailSender(logger());
    }

    @Bean
    public ICustomerService customerService() {
        CustomerService service = new CustomerService();
        service.setCustomerDAO(customerDAO());
        service.setEmailSender(emailSender());
        return service;
    }
}
