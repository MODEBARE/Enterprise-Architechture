package bank.service;

import bank.domain.Account;
import bank.domain.Customer;
import bank.integration.EmailSender;
import bank.repositories.AccountRepository;
import bank.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmailSender emailSender;

	public void createCustomerAndAccount(int customerId, String customerName, String emailAddress, String accountNumber) {
		try {
			persistCustomerAndAccount(customerId, customerName, emailAddress, accountNumber);
			emailSender.sendEmail(emailAddress, "Welcome " + customerName);
		} catch (Exception ex) {
			emailSender.sendEmail(emailAddress, "We could not create your account " + accountNumber);
		}
	}

	@Transactional
	public void persistCustomerAndAccount(int customerId, String customerName, String emailAddress, String accountNumber) {
		Account account = new Account(accountNumber);
		accountRepository.save(account);

		Customer customer = new Customer(customerId, customerName);
		customer.setAccount(account);
		customerRepository.saveCustomer(customer);
	}
}
