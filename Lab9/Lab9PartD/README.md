# Lab 9 Part D - Kafka Bank Application System

This project implements Part D of Lab 9, featuring a complete banking system using Kafka messaging for communication between client and bank applications.

## Architecture

- **KafkaBankApplication**: Processes banking operations received via Kafka messages
- **BankClientApplication**: Sends banking operation requests to the bank via Kafka
- **Apache Kafka**: Message broker for reliable communication

## Features

### Banking Operations
1. **Create Account**: Create new bank accounts with initial balance
2. **Deposit Money**: Add funds to existing accounts
3. **Withdraw Money**: Remove funds from accounts (with balance validation)

### Messaging
- **Asynchronous Processing**: Client sends requests via Kafka, bank processes them asynchronously
- **JSON Serialization**: All operations are sent as JSON messages
- **Error Handling**: Comprehensive validation and error messages
- **No Response Required**: One-way communication (client → bank)

## Prerequisites

1. **Apache Kafka**: Must be running on `localhost:9092`
   ```bash
   docker run -d --name=kafka -p 9092:9092 apache/kafka
   ```

2. **Java 21** and **Maven** installed

## Project Structure

```
Lab9PartD/
├── KafkaBankApplication/
│   ├── pom.xml
│   └── src/main/
│       ├── java/bank/
│       │   ├── KafkaBankApplication.java
│       │   ├── BankService.java
│       │   ├── BankOperationListener.java
│       │   ├── Account.java
│       │   └── BankOperation.java
│       └── resources/
│           └── application.properties
└── BankClientApplication/
    ├── pom.xml
    └── src/main/
        ├── java/client/
        │   ├── BankClientApplication.java
        │   ├── BankClientService.java
        │   └── BankOperation.java
        └── resources/
            └── application.properties
```

## How to Run

### Step 1: Start Kafka
```bash
docker run -d --name=kafka -p 9092:9092 apache/kafka
```

### Step 2: Start Bank Application (Terminal 1)
```bash
cd Lab9PartD/KafkaBankApplication
mvn spring-boot:run
```
The bank application will run on port 8084 and listen for operations on the `bankOperations` topic.

### Step 3: Run Client Application (Terminal 2)
```bash
cd Lab9PartD/BankClientApplication
mvn spring-boot:run
```
The client application will run on port 8085 and send demo operations to the bank.

## Demo Operations

The client automatically performs the following operations:

### Test 1: Account Creation
- ACC001: Alice Johnson ($1,000 initial balance)
- ACC002: Bob Smith ($2,500 initial balance)  
- ACC003: Carol Davis ($500 initial balance)

### Test 2: Deposits
- ACC001: +$500 → $1,500 total
- ACC002: +$1,000 → $3,500 total
- ACC003: +$250 → $750 total

### Test 3: Withdrawals
- ACC001: -$200 → $1,300 total
- ACC002: -$500 → $3,000 total
- ACC003: -$100 → $650 total

### Test 4: Error Conditions
- Deposit to non-existent account (ACC999)
- Withdraw more than available balance
- Create duplicate account

## Expected Output

### Bank Application Console:
```
🏦 Kafka Bank Application is running and waiting for operations...
✅ ACCOUNT CREATED:
   Account Number: ACC001
   Customer Name: Alice Johnson
   Initial Balance: $1000.0

💰 DEPOSIT SUCCESSFUL:
   Account Number: ACC001
   Deposit Amount: $500.0
   New Balance: $1500.0

💸 WITHDRAWAL SUCCESSFUL:
   Account Number: ACC001
   Withdrawal Amount: $200.0
   New Balance: $1300.0
```

### Client Application Console:
```
📤 Sending CREATE_ACCOUNT request:
   Account Number: ACC001
   Customer Name: Alice Johnson
   Initial Balance: $1000.0
✅ Request sent to bank via Kafka
```

## Kafka Topics

- **Topic**: `bankOperations`
- **Message Format**: JSON
- **Consumer Group**: `bank-group`

## Key Features Demonstrated

✅ **Kafka Messaging**: Complete producer-consumer setup  
✅ **JSON Serialization**: Structured data exchange  
✅ **Business Logic**: Full banking operations with validation  
✅ **Error Handling**: Comprehensive validation and error reporting  
✅ **Asynchronous Processing**: Non-blocking communication  
✅ **Spring Boot Integration**: Auto-configuration and dependency injection  

## Testing with KafkaMagic (Optional)

If you have KafkaMagic installed, you can:
1. Connect to `localhost:9092`
2. Monitor the `bankOperations` topic
3. View the JSON messages being sent from client to bank

This demonstrates the complete Kafka-based banking system as specified in Lab 9 Part D! 