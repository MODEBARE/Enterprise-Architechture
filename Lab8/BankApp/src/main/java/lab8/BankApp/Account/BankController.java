package lab8.BankApp.Account;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final Map<String, Account> accounts = new HashMap<>();

    @PostMapping("/create")
    public String createAccount(@RequestBody Account account) {
        accounts.put(account.getAccountNumber(), account);
        return "Account created.";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        Account acc = accounts.get(accountNumber);
        acc.setBalance(acc.getBalance() + amount);
        return "Deposit successful.";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        Account acc = accounts.get(accountNumber);
        if (acc.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient balance for withdrawal.");
        }
        acc.setBalance(acc.getBalance() - amount);
        return "Withdrawal successful.";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String from, @RequestParam String to, @RequestParam double amount) {
        Account sender = accounts.get(from);
        Account receiver = accounts.get(to);
        if (sender.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient balance for transfer.");
        }
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        return "Transfer successful.";
    }

    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathVariable String accountNumber) {
        return accounts.get(accountNumber);
    }
}
