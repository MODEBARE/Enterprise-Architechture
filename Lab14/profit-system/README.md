# Profit System - Spring AI Tool Calling Demo

A comprehensive REST application system demonstrating Spring AI ChatClient with tool calling functionality for company profit analysis and currency conversion.

## System Architecture

The system consists of three microservices:

1. **Profit Service** (Port 8081) - Stores and retrieves monthly profit data
2. **Currency Service** (Port 8082) - Handles currency conversion
3. **Company Service** (Port 8083) - LLM-powered service using Spring AI ChatClient with tool calling

## Features

- **Profit Data Management**: Store and retrieve monthly profit data with H2 database
- **Currency Conversion**: Convert profits to different currencies
- **LLM Tool Calling**: Spring AI ChatClient that automatically calls tools to answer profit-related questions
- **HTTP Request Logging**: ClientHttpRequestInterceptor logs all HTTP requests/responses
- **Multi-Currency Support**: Query profits in any supported currency

## Prerequisites

- Java 17+
- Maven 3.6+
- OpenAI API Key (for LLM functionality)

## Quick Start

### 1. Set Environment Variable

```bash
export OPENAI_API_KEY=your_openai_api_key_here
```

### 2. Start Services

Start each service in separate terminals:

```bash
# Terminal 1 - Profit Service
cd profit-service
mvn spring-boot:run

# Terminal 2 - Currency Service  
cd currency-service
mvn spring-boot:run

# Terminal 3 - Company Service
cd company-service
mvn spring-boot:run
```

### 3. Test the System

The services will be available at:
- Profit Service: http://localhost:8081
- Currency Service: http://localhost:8082
- Company Service: http://localhost:8083

## API Endpoints

### Profit Service (Port 8081)

```bash
# Get profit for specific month
GET /api/profit/month/{month}
# Example: GET /api/profit/month/2024-01

# Get all profits
GET /api/profit/all

# Get profits for specific year
GET /api/profit/year/{year}
# Example: GET /api/profit/year/2024

# Get total profit for year
GET /api/profit/year/{year}/total
# Example: GET /api/profit/year/2024/total

# Health check
GET /api/profit/health
```

### Currency Service (Port 8082)

```bash
# Convert currency
GET /api/currency/convert?amount=1000&from=USD&to=EUR
POST /api/currency/convert
{
  "amount": 1000,
  "fromCurrency": "USD",
  "toCurrency": "EUR"
}

# Get exchange rate
GET /api/currency/rate?from=USD&to=EUR

# Get supported currencies
GET /api/currency/supported

# Health check
GET /api/currency/health
```

### Company Service (Port 8083) - LLM with Tool Calling

```bash
# Ask questions about company profits
GET /api/company/query?question=What was the profit in January 2024?&currency=USD

POST /api/company/query
{
  "question": "What was our best performing month?",
  "currency": "EUR"
}

# Get example questions
GET /api/company/examples

# Health check
GET /api/company/health
```

## Example Questions for LLM

The Company Service LLM can answer questions like:

- "What was the profit in January 2024?"
- "Show me the total profit for 2023"
- "What was our best performing month?"
- "Which month had the lowest profit?"
- "What is the overall profit across all months?"
- "Convert the profit for December 2024 to EUR"
- "Show me yearly profit in GBP"

## Tool Calling Architecture

The Company Service uses Spring AI ChatClient with two custom tools:

### ProfitTool
- **Purpose**: Retrieves profit data from the Profit Service
- **Parameters**: month, year, type (monthly/yearly/all/best/worst), currency
- **Function**: Automatically called when LLM needs profit data

### CurrencyTool  
- **Purpose**: Converts amounts between currencies
- **Parameters**: amount, fromCurrency, toCurrency
- **Function**: Automatically called when LLM needs currency conversion

## HTTP Request Logging

The Company Service includes a `LoggingClientHttpRequestInterceptor` that logs:
- Request method, URI, headers, and body
- Response status, headers, and body

Check the application logs to see HTTP communication between services.

## Supported Currencies

- USD (US Dollar) - Base currency
- EUR (Euro)
- GBP (British Pound)
- CAD (Canadian Dollar)
- AUD (Australian Dollar)
- JPY (Japanese Yen)
- CHF (Swiss Franc)
- CNY (Chinese Yuan)
- INR (Indian Rupee)
- BRL (Brazilian Real)
- MXN (Mexican Peso)
- KRW (South Korean Won)

## Sample Data

The Profit Service comes pre-loaded with sample profit data for 2023 and 2024:
- Monthly profits ranging from $78,000 to $175,000
- Descriptive information for each month
- Various seasonal patterns and business events

## Testing Examples

### 1. Basic Profit Query
```bash
curl "http://localhost:8083/api/company/query?question=What was the profit in January 2024?"
```

### 2. Currency Conversion Query
```bash
curl -X POST http://localhost:8083/api/company/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What was our best performing month in EUR?",
    "currency": "EUR"
  }'
```

### 3. Yearly Analysis
```bash
curl "http://localhost:8083/api/company/query?question=Show me the total profit for 2023 in GBP&currency=GBP"
```

## Configuration

### OpenAI Configuration
Edit `company-service/src/main/resources/application.yml`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-3.5-turbo
          temperature: 0.3
```

### Service URLs
Configure service URLs in application.yml:

```yaml
profit:
  service:
    url: http://localhost:8081

currency:
  service:
    url: http://localhost:8082
```

## Troubleshooting

### Common Issues

1. **OpenAI API Key Not Set**
   - Ensure `OPENAI_API_KEY` environment variable is set
   - Check application logs for authentication errors

2. **Service Connection Issues**
   - Verify all services are running on correct ports
   - Check service URLs in configuration

3. **H2 Database Issues**
   - Access H2 console at http://localhost:8081/h2-console
   - JDBC URL: `jdbc:h2:mem:profitdb`
   - Username: `sa`, Password: (empty)

4. **Tool Calling Issues**
   - Check logs for tool execution details
   - Verify tool parameters match expected format

### Logs

Monitor application logs for:
- HTTP request/response details
- Tool calling execution
- LLM interaction traces
- Service communication

## Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User/Client   │    │  Company Service │    │  Profit Service │
│                 │    │   (Port 8083)    │    │   (Port 8081)   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          │ Question             │ HTTP Requests        │ H2 Database
          │                      │ (with Interceptor)   │
          ▼                      ▼                      ▼
    ┌─────────────┐    ┌─────────────────┐    ┌─────────────────┐
    │ REST API    │    │ Spring AI       │    │ Monthly Profit  │
    │             │    │ ChatClient      │    │ Data            │
    └─────────────┘    │ + Tools         │    └─────────────────┘
                       └─────────┬───────┘
                                 │
                                 │ HTTP Requests
                                 │ (with Interceptor)
                                 ▼
                       ┌─────────────────┐
                       │ Currency Service│
                       │   (Port 8082)   │
                       │                 │
                       │ Exchange Rates  │
                       └─────────────────┘
```

## License

This project is for educational purposes demonstrating Spring AI tool calling capabilities.