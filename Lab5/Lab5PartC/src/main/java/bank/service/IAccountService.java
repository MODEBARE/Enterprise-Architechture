package bank.service;

import java.util.Collection;

import bank.dto.AccountDTO;

public interface IAccountService {
    AccountDTO createAccount(long accountNumber, String customerName);
    void deposit(long accountNumber, double amount);
    AccountDTO getAccount(long accountNumber);
    Collection<AccountDTO> getAllAccounts();
    void withdraw(long accountNumber, double amount);
    void depositEuros(long accountNumber, double amount);
    void withdrawEuros(long accountNumber, double amount);
    void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description);
}
