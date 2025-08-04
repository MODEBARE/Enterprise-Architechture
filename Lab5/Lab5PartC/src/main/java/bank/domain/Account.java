package bank.domain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "account")
public class Account {
	
	@Id
	@Column(name = "account_number")
	private long accountnumber;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<AccountEntry> entryList = new ArrayList<AccountEntry>();
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	public Account() {
	}
	
	public Account (long accountnr){
		this.accountnumber = accountnr;
	}
	
	public long getAccountnumber() {
		return accountnumber;
	}
	
	public void setAccountnumber(long accountnumber) {
		this.accountnumber = accountnumber;
	}
	
	public double getBalance() {
		double balance=0;
		for (AccountEntry entry : entryList) {
			balance+=entry.getAmount();
		}
		return balance;
	}
	
	public void deposit(double amount){
		AccountEntry entry = new AccountEntry(new Date(), amount, "deposit", "", "");
		entry.setAccount(this);
		entryList.add(entry);
	}
	
	public void withdraw(double amount){
		AccountEntry entry = new AccountEntry(new Date(), -amount, "withdraw", "", "");
		entry.setAccount(this);
		entryList.add(entry);	
	}

	private void addEntry(AccountEntry entry){
		entry.setAccount(this);
		entryList.add(entry);
	}

	public void transferFunds(Account toAccount, double amount, String description){
		AccountEntry fromEntry = new AccountEntry(new Date(), -amount, description, ""+toAccount.getAccountnumber(), toAccount.getCustomer().getName());
		AccountEntry toEntry = new AccountEntry(new Date(), amount, description, ""+this.getAccountnumber(), this.getCustomer().getName());
		fromEntry.setAccount(this);
		toEntry.setAccount(toAccount);
		entryList.add(fromEntry);	
		toAccount.addEntry(toEntry);
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Collection<AccountEntry> getEntryList() {
		return entryList;
	}

	public void setEntryList(Collection<AccountEntry> entryList) {
		this.entryList = entryList;
	}
}
