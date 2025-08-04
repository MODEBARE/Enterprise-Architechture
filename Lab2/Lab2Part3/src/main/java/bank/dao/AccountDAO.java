package bank.dao;

import java.util.*;
import bank.domain.Account;
import bank.logging.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO implements IAccountDAO {
	private final Collection<Account> accountlist = new ArrayList<>();
	private final ILogger logger;

	@Autowired
	public AccountDAO(ILogger logger) {
		this.logger = logger;
	}

	public void saveAccount(Account account) {
		accountlist.add(account);
		logger.log("Account is saved with accountnr = " + account.getAccountnumber());
	}

	public void updateAccount(Account account) {
		Account accountexist = loadAccount(account.getAccountnumber());
		if (accountexist != null) {
			accountlist.remove(accountexist);
			accountlist.add(account);
			logger.log("Account is updated with accountnr = " + account.getAccountnumber());
		}
	}

	public Account loadAccount(long accountnumber) {
		for (Account account : accountlist) {
			if (account.getAccountnumber() == accountnumber) {
				logger.log("Account is loaded with accountnr = " + accountnumber);
				return account;
			}
		}
		return null;
	}

	public Collection<Account> getAccounts() {
		logger.log("All accounts retrieved");
		return accountlist;
	}
}
