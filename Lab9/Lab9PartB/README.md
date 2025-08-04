# Lab 9 Part B - JMS Bank Application with Tax Service

This project implements Part B of Lab 9, featuring a bank application that integrates with a tax service using JMS messaging.

## Architecture

- **BankApplication**: Handles bank deposits and sends JMS messages to TaxService for deposits ≥ 10,000 EUR
- **TaxService**: Receives JMS messages from the bank and logs tax reporting information
- **ActiveMQ**: Message broker for JMS communication

## Prerequisites

1. **ActiveMQ**: Must be running on `tcp://localhost:61616`
   ```bash
   docker run -d --name activemq -p 61616:61616 -p 8161:8161 apache/activemq-classic
   ```

2. **Java 21** and **Maven** installed

## Project Structure

```
Lab9PartB/
├── TaxService/
│   ├── pom.xml
│   └── src/main/
│       ├── java/taxservice/
│       │   ├── TaxServiceApplication.java
│       │   ├── TaxMessageListener.java
│       │   └── DepositMessage.java
│       └── resources/
│           └── application.properties
└── BankApplication/
    ├── pom.xml
    └── src/main/
        ├── java/bank/
        │   ├── BankApplication.java
        │   ├── AccountService.java
        │   ├── Account.java
        │   └── DepositMessage.java
        └── resources/
            └── application.properties
```

## How to Run

### Step 1: Start ActiveMQ
```bash
docker run -d --name activemq -p 61616:61616 -p 8161:8161 apache/activemq-classic
```

### Step 2: Start TaxService (Terminal 1)
```bash
cd Lab9PartB/TaxService
mvn spring-boot:run
```
TaxService will run on port 8081 and wait for JMS messages.

### Step 3: Run BankApplication (Terminal 2)
```bash
cd Lab9PartB/BankApplication
mvn spring-boot:run
```
BankApplication will run on port 8080 and perform demo transactions.

## What Happens

1. **BankApplication** starts with 3 demo accounts:
   - ACC001: John Doe (5,000 EUR)
   - ACC002: Jane Smith (15,000 EUR)
   - ACC003: Bob Johnson (2,500 EUR)

2. **Demo Transactions**:
   - 5,000 EUR deposit → No tax notification (below threshold)
   - 7,500 EUR deposit → No tax notification (below threshold)
   - 15,000 EUR deposit → **Tax notification sent** (above threshold)
   - 10,000 EUR deposit → **Tax notification sent** (exactly at threshold)

3. **TaxService** receives JMS messages for large deposits and logs:
   - Account number
   - Deposit amount
   - Currency (EUR)
   - Timestamp
   - Tax reporting confirmation

## Key Features

- **Tax Reporting Threshold**: 10,000 EUR or greater
- **JMS Queue**: `taxQueue` for reliable message delivery
- **JSON Serialization**: Messages are sent as JSON strings
- **Error Handling**: Graceful handling of message conversion errors
- **Demo Data**: Pre-populated accounts for testing

## Dependencies Used

Both applications include:
- `spring-boot-starter-activemq`
- `jackson-databind` for JSON serialization

## ActiveMQ Web Console

Access the ActiveMQ web console at:
- URL: http://localhost:8161/admin
- Username: admin
- Password: admin

Monitor the `taxQueue` to see message flow.

## Testing

The demo automatically tests:
1. ✅ Small deposits (no tax notification)
2. ✅ Large deposits (tax notification triggered)
3. ✅ JMS message sending and receiving
4. ✅ JSON serialization/deserialization
5. ✅ Error handling

Check both console outputs to see the complete flow! 