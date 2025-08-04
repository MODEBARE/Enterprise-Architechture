package bank.service;

import java.util.Collection;
import java.util.stream.Collectors;

import bank.domain.Account;
import bank.domain.Customer;
import bank.dto.AccountDTO;
import bank.jms.IJMSSender;
import bank.logging.ILogger;
import bank.repository.AccountRepository;
import bank.repository.CustomerRepository;
import bank.util.DTOMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService implements IAccountService {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final ICurrencyConverter currencyConverter;
	private final IJMSSender jmsSender;
	private final ILogger logger;
	private final DTOMapper dtoMapper;

	@Autowired
	public AccountService(AccountRepository accountRepository,
						  CustomerRepository customerRepository,
						  ICurrencyConverter currencyConverter,
						  IJMSSender jmsSender,
						  ILogger logger,
						  DTOMapper dtoMapper) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.currencyConverter = currencyConverter;
		this.jmsSender = jmsSender;
		this.logger = logger;
		this.dtoMapper = dtoMapper;
	}

	public AccountDTO createAccount(long accountNumber, String customerName) {
		Account account = new Account(accountNumber);
		Customer customer = new Customer(customerName);
		
		// Save customer first to get ID
		customer = customerRepository.save(customer);
		account.setCustomer(customer);
		
		// Save account
		account = accountRepository.save(account);
		
		logger.log("createAccount with parameters accountNumber= " + accountNumber + " , customerName= " + customerName);
		return dtoMapper.toAccountDTO(account);
	}

	public void deposit(long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumberWithEntries(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
		
		account.deposit(amount);
		accountRepository.save(account);
		
		logger.log("deposit with parameters accountNumber= " + accountNumber + " , amount= " + amount);
		if (amount > 10000) {
			jmsSender.sendJMSMessage("Deposit of $ " + amount + " to account with accountNumber= " + accountNumber);
		}
	}

	public AccountDTO getAccount(long accountNumber) {
		Account account = accountRepository.findByAccountnumberWithEntries(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
		return dtoMapper.toAccountDTO(account);
	}

	public Collection<AccountDTO> getAllAccounts() {
		return accountRepository.findAll().stream()
				.map(dtoMapper::toAccountDTO)
				.collect(Collectors.toList());
	}

	public void withdraw(long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumberWithEntries(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
		
		account.withdraw(amount);
		accountRepository.save(account);
		
		logger.log("withdraw with parameters accountNumber= " + accountNumber + " , amount= " + amount);
	}

	public void depositEuros(long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumberWithEntries(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
		
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.deposit(amountDollars);
		accountRepository.save(account);
		
		logger.log("depositEuros with parameters accountNumber= " + accountNumber + " , amount= " + amount);
		if (amountDollars > 10000) {
			jmsSender.sendJMSMessage("Deposit of $ " + amountDollars + " to account with accountNumber= " + accountNumber);
		}
	}

	public void withdrawEuros(long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumberWithEntries(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
		
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.withdraw(amountDollars);
		accountRepository.save(account);
		
		logger.log("withdrawEuros with parameters accountNumber= " + accountNumber + " , amount= " + amount);
	}

	public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description) {
		Account fromAccount = accountRepository.findByAccountnumberWithEntries(fromAccountNumber)
				.orElseThrow(() -> new RuntimeException("From account not found: " + fromAccountNumber));
		Account toAccount = accountRepository.findByAccountnumberWithEntries(toAccountNumber)
				.orElseThrow(() -> new RuntimeException("To account not found: " + toAccountNumber));
		
		fromAccount.transferFunds(toAccount, amount, description);
		
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		
		logger.log("transferFunds with parameters fromAccountNumber= " + fromAccountNumber + 
				  " , toAccountNumber= " + toAccountNumber + " , amount= " + amount + " , description= " + description);
		
		if (amount > 10000) {
			jmsSender.sendJMSMessage("TransferFunds of $ " + amount + 
					" from account with accountNumber= " + fromAccountNumber + 
					" to account with accountNumber= " + toAccountNumber);
		}
	}
}
