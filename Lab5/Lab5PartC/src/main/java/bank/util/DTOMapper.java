package bank.util;

import bank.domain.Account;
import bank.domain.AccountEntry;
import bank.domain.Customer;
import bank.dto.AccountDTO;
import bank.dto.AccountEntryDTO;
import bank.dto.CustomerDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DTOMapper {

    public CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) return null;
        
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        return dto;
    }

    public Customer toCustomer(CustomerDTO dto) {
        if (dto == null) return null;
        
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        return customer;
    }

    public AccountEntryDTO toAccountEntryDTO(AccountEntry entry) {
        if (entry == null) return null;
        
        AccountEntryDTO dto = new AccountEntryDTO();
        dto.setId(entry.getId());
        dto.setDate(entry.getDate());
        dto.setAmount(entry.getAmount());
        dto.setDescription(entry.getDescription());
        dto.setFromAccountNumber(entry.getFromAccountNumber());
        dto.setFromPersonName(entry.getFromPersonName());
        return dto;
    }

    public AccountEntry toAccountEntry(AccountEntryDTO dto) {
        if (dto == null) return null;
        
        AccountEntry entry = new AccountEntry();
        entry.setId(dto.getId());
        entry.setDate(dto.getDate());
        entry.setAmount(dto.getAmount());
        entry.setDescription(dto.getDescription());
        entry.setFromAccountNumber(dto.getFromAccountNumber());
        entry.setFromPersonName(dto.getFromPersonName());
        return entry;
    }

    public AccountDTO toAccountDTO(Account account) {
        if (account == null) return null;
        
        AccountDTO dto = new AccountDTO();
        dto.setAccountnumber(account.getAccountnumber());
        dto.setCustomer(toCustomerDTO(account.getCustomer()));
        dto.setBalance(account.getBalance());
        
        if (account.getEntryList() != null) {
            dto.setEntryList(account.getEntryList().stream()
                    .map(this::toAccountEntryDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public Account toAccount(AccountDTO dto) {
        if (dto == null) return null;
        
        Account account = new Account();
        account.setAccountnumber(dto.getAccountnumber());
        account.setCustomer(toCustomer(dto.getCustomer()));
        
        if (dto.getEntryList() != null) {
            account.setEntryList(dto.getEntryList().stream()
                    .map(this::toAccountEntry)
                    .collect(Collectors.toList()));
        }
        
        return account;
    }
} 