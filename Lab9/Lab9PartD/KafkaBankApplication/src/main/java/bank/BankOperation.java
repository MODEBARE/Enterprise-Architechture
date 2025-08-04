package bank;

public class BankOperation {
    private String operationType; // "CREATE_ACCOUNT", "DEPOSIT", "WITHDRAW"
    private String accountNumber;
    private String customerName;
    private double amount;

    public BankOperation() {
    }

    public BankOperation(String operationType, String accountNumber, String customerName, double amount) {
        this.operationType = operationType;
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BankOperation{" +
                "operationType='" + operationType + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                '}';
    }
} 