package bank.dto;

import java.util.ArrayList;
import java.util.Collection;

public class AccountDTO {
    private long accountnumber;
    private Collection<AccountEntryDTO> entryList = new ArrayList<>();
    private CustomerDTO customer;
    private double balance;

    public AccountDTO() {
    }

    public AccountDTO(long accountnumber) {
        this.accountnumber = accountnumber;
    }

    public long getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(long accountnumber) {
        this.accountnumber = accountnumber;
    }

    public Collection<AccountEntryDTO> getEntryList() {
        return entryList;
    }

    public void setEntryList(Collection<AccountEntryDTO> entryList) {
        this.entryList = entryList;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void calculateBalance() {
        double calculatedBalance = 0;
        for (AccountEntryDTO entry : entryList) {
            calculatedBalance += entry.getAmount();
        }
        this.balance = calculatedBalance;
    }
} 