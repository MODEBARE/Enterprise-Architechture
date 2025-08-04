package bank.service;

import bank.domain.Account;
import bank.domain.Customer;
import bank.domain.TraceRecord;
import bank.integration.EmailSender;
import bank.repositories.AccountRepository;
import bank.repositories.CustomerRepository;
import bank.repositories.TraceRecordRepository;
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
	private TraceRecordRepository traceRecordRepository;

	@Autowired
	private EmailSender emailSender;

	public void createCustomerAndAccount(int customerId, String customerName, String emailAddress, String accountNumber) {
		try {
			persistCustomerAndAccount(customerId, customerName, accountNumber);
			emailSender.sendEmail(emailAddress, "Welcome " + customerName);

			// ✅ Save success trace
			String successMessage = "Customer " + customerName + " created with account " + accountNumber;
			traceRecordRepository.save(new TraceRecord(successMessage));

		} catch (Exception ex) {
			emailSender.sendEmail(emailAddress, "We could not create your account " + accountNumber);

			// ❌ Save failure trace
			String failureMessage = "Could not create customer " + customerName + " with account " + accountNumber;
			traceRecordRepository.save(new TraceRecord(failureMessage));
		}
	}

	@Transactional
	public void persistCustomerAndAccount(int customerId, String customerName, String accountNumber) {
		Account account = new Account(accountNumber);
		accountRepository.save(account);

		Customer customer = new Customer(customerId, customerName);
		customer.setAccount(account);
		customerRepository.saveCustomer(customer);
	}
}
