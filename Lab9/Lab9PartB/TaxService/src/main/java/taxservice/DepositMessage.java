package taxservice;

public class DepositMessage {
    private String accountNumber;
    private double amount;
    private String currency;
    private String timestamp;

    public DepositMessage() {
    }

    public DepositMessage(String accountNumber, double amount, String currency, String timestamp) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DepositMessage{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
} 