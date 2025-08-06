# Lab 12 Client Application

This is a standalone Spring Boot client application that calls the Lab 12 server application (Part 1) to demonstrate role-based access control with different user credentials.

## Overview

The client application tests all three endpoints from the Lab 12 server:
- `/shop` - Public endpoint (no authentication required)
- `/orders` - Employee endpoint (requires EMPLOYEE role)
- `/payments` - Finance endpoint (requires FINANCE role)

## Project Structure

```
Lab12Client/
├── src/main/java/com/example/client/
│   ├── Lab12ClientApplication.java     # Main application class
│   └── Lab12RestClient.java           # REST client service
├── src/main/resources/
│   └── application.properties         # Application configuration
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## Features

- **Automated Testing**: Tests all endpoints with different credentials
- **Basic Authentication**: Implements HTTP Basic Auth for secure endpoints
- **Comprehensive Logging**: Shows detailed results for each test
- **Error Handling**: Properly handles authentication and authorization failures
- **Spring Boot**: Uses Spring Boot framework with RestTemplate

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6** or higher
3. **Lab 12 Server Application** running on `http://localhost:8080`

## Running the Client

### Step 1: Start the Lab 12 Server
First, make sure the Lab 12 server application is running:

```bash
cd /Users/elijahariyibi/Desktop/Lab12PartA
mvn spring-boot:run
```

The server should start on `http://localhost:8080`

### Step 2: Run the Client Application
In a new terminal, run the client:

```bash
cd /Users/elijahariyibi/Desktop/Lab12Client
mvn clean compile
mvn spring-boot:run
```

## Test Scenarios

The client application automatically runs the following test scenarios:

### 1. Public Endpoint Test
- **Endpoint**: `GET /shop`
- **Authentication**: None required
- **Expected Result**: ✅ SUCCESS

### 2. Bob's Credentials Test
- **User**: `bob` (password: `password`)
- **Roles**: EMPLOYEE, SALES
- **Tests**:
  - `GET /shop` → ✅ SUCCESS (public)
  - `GET /orders` → ✅ SUCCESS (has EMPLOYEE role)
  - `GET /payments` → ✅ CORRECTLY DENIED (no FINANCE role)

### 3. Mary's Credentials Test
- **User**: `mary` (password: `password`)
- **Roles**: EMPLOYEE, FINANCE
- **Tests**:
  - `GET /shop` → ✅ SUCCESS (public)
  - `GET /orders` → ✅ SUCCESS (has EMPLOYEE role)
  - `GET /payments` → ✅ SUCCESS (has FINANCE role)

### 4. Unauthorized Access Tests
- **Wrong Credentials**: `wrong:credentials`
- **No Authentication**: No auth headers
- **Expected Results**: ✅ CORRECTLY DENIED for all protected endpoints

## Sample Output

```
================================================================================
LAB 12 CLIENT - TESTING SERVER ENDPOINTS
================================================================================

1. TESTING PUBLIC ENDPOINT
----------------------------------------
✅ GET /shop (no auth): SUCCESS
   Response: Welcome to the Shop! This endpoint is accessible by everyone.

2. TESTING WITH BOB'S CREDENTIALS (EMPLOYEE, SALES)
--------------------------------------------------
✅ GET /shop (Bob): SUCCESS
   Status: 200 OK
   Response: Welcome to the Shop! This endpoint is accessible by everyone.
✅ GET /orders (Bob): SUCCESS
   Status: 200 OK
   Response: Orders endpoint - accessible by all employees. Current user: bob with authorities: [ROLE_EMPLOYEE, ROLE_SALES]
✅ GET /payments (Bob): CORRECTLY DENIED
   Status: 403 FORBIDDEN
   Error: Forbidden

3. TESTING WITH MARY'S CREDENTIALS (EMPLOYEE, FINANCE)
----------------------------------------------------
✅ GET /shop (Mary): SUCCESS
   Status: 200 OK
   Response: Welcome to the Shop! This endpoint is accessible by everyone.
✅ GET /orders (Mary): SUCCESS
   Status: 200 OK
   Response: Orders endpoint - accessible by all employees. Current user: mary with authorities: [ROLE_EMPLOYEE, ROLE_FINANCE]
✅ GET /payments (Mary): SUCCESS
   Status: 200 OK
   Response: Payments endpoint - accessible only by finance department employees. Current user: mary with authorities: [ROLE_EMPLOYEE, ROLE_FINANCE]

4. TESTING UNAUTHORIZED ACCESS
------------------------------
✅ GET /orders (Wrong User): CORRECTLY DENIED
   Status: 401 UNAUTHORIZED
   Error: Unauthorized
✅ GET /orders (No Auth): CORRECTLY DENIED
   Status: 401 UNAUTHORIZED
   Error: Unauthorized

================================================================================
CLIENT TESTING COMPLETED
================================================================================
```

## Technical Implementation

### RestTemplate Configuration
The client uses Spring's `RestTemplate` to make HTTP requests:

```java
private final RestTemplate restTemplate = new RestTemplate();
```

### Basic Authentication
HTTP Basic Authentication is implemented using Base64 encoding:

```java
private HttpHeaders createAuthHeaders(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    String credentials = username + ":" + password;
    String encodedCredentials = Base64.getEncoder().encodeToString(
        credentials.getBytes(StandardCharsets.UTF_8)
    );
    headers.set("Authorization", "Basic " + encodedCredentials);
    return headers;
}
```

### Error Handling
The client properly handles different HTTP status codes:
- **200 OK**: Successful requests
- **401 Unauthorized**: Missing or invalid credentials
- **403 Forbidden**: Valid credentials but insufficient permissions

## Dependencies

- **Spring Boot Starter**: Core Spring Boot functionality
- **Spring Boot Starter Web**: Web client capabilities
- **Spring Web**: RestTemplate and HTTP utilities
- **Jackson Databind**: JSON processing
- **Spring Boot Starter Test**: Testing utilities

## Configuration

The application is configured as a non-web application:
```properties
spring.main.web-application-type=none
```

This means it runs as a standalone client and exits after completing the tests.

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Make sure the Lab 12 server is running on `http://localhost:8080`
   - Check if the server started successfully

2. **Compilation Errors**
   - Ensure Java 17 or higher is installed
   - Run `mvn clean compile` to refresh dependencies

3. **Authentication Failures**
   - Verify the server has the correct users (bob, mary) with proper roles
   - Check server logs for authentication details

### Debug Mode
To enable debug logging, add this to `application.properties`:
```properties
logging.level.org.springframework.web.client=DEBUG
```

## Extending the Client

To add more test scenarios:

1. **Add New Users**: Test with additional user credentials
2. **Add New Endpoints**: Test additional server endpoints
3. **Add Different HTTP Methods**: Test POST, PUT, DELETE requests
4. **Add Custom Headers**: Test with additional HTTP headers

## Related Projects

- **Lab12PartA**: The server application being tested
- **Lab12PartB**: Additional server functionality (if applicable)