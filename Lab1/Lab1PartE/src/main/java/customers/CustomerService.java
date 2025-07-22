package customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerService implements ICustomerService {
	private ICustomerDAO customerDAO;
	private IEmailSender emailSender;

	// Setter for CustomerDAO
	@Autowired
	public void setCustomerDAO(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	// Setter for EmailSender
	@Autowired
	public void setEmailSender(IEmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void addCustomer(String name, String email, String street,
							String city, String zip) {
		Customer customer = new Customer(name, email);
		Address address = new Address(street, city, zip);
		customer.setAddress(address);
		customerDAO.save(customer);
		emailSender.sendEmail(email, "Welcome " + name + " as a new customer");
	}
}
