# Lab 12 Part C Client Application

This is a standalone Spring Boot client application that calls the Lab 12 Part C JWT server application to demonstrate JWT-based authentication and role-based authorization with different user credentials.

## Overview

The client application tests JWT authentication and authorization with the Lab 12 Part C server:
- **Authentication**: Login/register to get JWT tokens
- **Authorization**: Access endpoints based on user roles (USER, ADMIN)

## Lab 12 Part C Server Endpoints

### Authentication Endpoints
- `POST /auth/signup` - Register new user
- `POST /auth/signin` - Login and receive JWT token

### Protected Endpoints
- `GET /api/all` - Public endpoint (no authentication required)
- `GET /api/users` - Requires USER role
- `GET /api/admins` - Requires ADMIN role

## Project Structure

```
Lab12PartCClient/
├── src/main/java/com/example/jwtclient/
│   ├── Lab12PartCClientApplication.java    # Main application class
│   ├── JwtRestClient.java                  # JWT REST client service
│   └── dto/                               # Data Transfer Objects
│       ├── SignUpRequest.java             # Registration request
│       ├── SignInRequest.java             # Login request
│       └── JwtAuthenticationResponse.java  # JWT token response
├── src/main/resources/
│   └── application.properties             # Application configuration
├── pom.xml                               # Maven configuration
└── README.md                             # This file
```

## Features

- **JWT Authentication**: Implements registration and login with JWT tokens
- **Bearer Token Authorization**: Uses JWT tokens in Authorization headers
- **Role-Based Testing**: Tests endpoints with different user roles
- **Comprehensive Logging**: Shows detailed results for each test
- **Error Handling**: Properly handles authentication and authorization failures
- **Spring Boot Framework**: Uses RestTemplate for HTTP communications

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6** or higher
3. **Lab 12 Part C Server** running on `http://localhost:8080`
4. **Docker Desktop** running (required by the server for database)

## Running the Client

### Step 1: Start the Lab 12 Part C Server
First, make sure the Lab 12 Part C server application is running:

```bash
cd /Users/elijahariyibi/Desktop/Lab12PartC
# Make sure Docker Desktop is running
mvn spring-boot:run
```

The server should start on `http://localhost:8080`

### Step 2: Run the Client Application
In a new terminal, run the client:

```bash
cd /Users/elijahariyibi/Desktop/Lab12PartCClient
mvn clean compile
mvn spring-boot:run
```

## Test Scenarios

The client application automatically runs the following test scenarios:

### 1. Public Endpoint Test
- **Endpoint**: `GET /api/all`
- **Authentication**: None required
- **Expected Result**: ✅ SUCCESS

### 2. User Registration & Authentication
- **Action**: Register new user with email `user@example.com`
- **Fallback**: Login if user already exists
- **Result**: Receive JWT token for USER role

### 3. Admin Registration & Authentication
- **Action**: Register new admin with email `admin@example.com`
- **Fallback**: Login if admin already exists
- **Result**: Receive JWT token for ADMIN role

### 4. User Role Tests
- **User**: JWT token from user registration/login
- **Tests**:
  - `GET /api/all` → ✅ SUCCESS (public)
  - `GET /api/users` → ✅ SUCCESS (has USER role)
  - `GET /api/admins` → ✅ CORRECTLY DENIED (no ADMIN role)

### 5. Admin Role Tests
- **User**: JWT token from admin registration/login
- **Tests**:
  - `GET /api/all` → ✅ SUCCESS (public)
  - `GET /api/users` → ✅ SUCCESS (if admin has USER role)
  - `GET /api/admins` → ✅ SUCCESS (has ADMIN role)

### 6. Unauthorized Access Tests
- **Invalid JWT Token**: Test with malformed token
- **No Token**: Test without Authorization header
- **Expected Results**: ✅ CORRECTLY DENIED for all protected endpoints

## Sample Output

```
================================================================================
LAB 12 PART C CLIENT - TESTING JWT SERVER ENDPOINTS
================================================================================

1. TESTING PUBLIC ENDPOINT
----------------------------------------
✅ GET /api/all (no auth): SUCCESS
   Response: everyone can see this

2. REGISTERING AND AUTHENTICATING USER USER
--------------------------------------------------
✅ User registration: SUCCESS
   JWT Token received: eyJhbGciOiJIUzI1NiJ9...

3. REGISTERING AND AUTHENTICATING ADMIN USER
----------------------------------------------------
✅ Admin registration: SUCCESS
   JWT Token received: eyJhbGciOiJIUzI1NiJ9...

4. TESTING WITH USER JWT TOKEN
-----------------------------------
✅ GET /api/all (User): SUCCESS
   Status: 200 OK
   Response: everyone can see this
✅ GET /api/users (User): SUCCESS
   Status: 200 OK
   Response: ONLY users can see this
✅ GET /api/admins (User): CORRECTLY DENIED
   Status: 403 FORBIDDEN
   Error: Forbidden

5. TESTING WITH ADMIN JWT TOKEN
------------------------------------
✅ GET /api/all (Admin): SUCCESS
   Status: 200 OK
   Response: everyone can see this
✅ GET /api/users (Admin): SUCCESS
   Status: 200 OK
   Response: ONLY users can see this
✅ GET /api/admins (Admin): SUCCESS
   Status: 200 OK
   Response: ONLY admins can see this

6. TESTING UNAUTHORIZED ACCESS
------------------------------
✅ GET /api/users (Invalid Token): CORRECTLY DENIED
   Status: 401 UNAUTHORIZED
   Error: Unauthorized
✅ GET /api/users (No Token): CORRECTLY DENIED
   Status: 401 UNAUTHORIZED
   Error: Unauthorized

================================================================================
JWT CLIENT TESTING COMPLETED
================================================================================
```

## Technical Implementation

### JWT Token Management
The client handles JWT tokens by:
1. **Registration/Login**: Sends credentials to get JWT token
2. **Storage**: Stores token in memory for subsequent requests
3. **Authorization**: Adds token to Authorization header as `Bearer <token>`

### Request/Response DTOs
The client uses DTOs that match the server's expected format:

```java
// Registration
SignUpRequest {
    firstName, lastName, email, password
}

// Login
SignInRequest {
    email, password
}

// Response
JwtAuthenticationResponse {
    token
}
```

### JWT Bearer Authentication
JWT tokens are sent in the Authorization header:

```java
private HttpHeaders createJwtHeaders(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
}
```

### Error Handling
The client properly handles different HTTP status codes:
- **200 OK**: Successful requests
- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: Valid token but insufficient permissions
- **409 Conflict**: User already exists during registration

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
   - Make sure the Lab 12 Part C server is running on `http://localhost:8080`
   - Ensure Docker Desktop is running (required by the server)

2. **JWT Token Errors**
   - Check if the server's JWT secret key is properly configured
   - Verify token format and expiration

3. **Database Connection Issues**
   - Make sure Docker Desktop is running
   - Check if the server's database containers are up

4. **Role Authorization Issues**
   - Verify user roles are properly assigned during registration
   - Check server's role-based security configuration

### Debug Mode
To enable debug logging, add this to `application.properties`:
```properties
logging.level.org.springframework.web.client=DEBUG
```

## JWT vs Basic Auth Comparison

This client demonstrates JWT-based authentication, which differs from the Lab 12 Part 1 client:

| Aspect | Basic Auth (Part 1) | JWT Auth (Part C) |
|--------|---------------------|-------------------|
| Authentication | Username:Password in each request | Login once, get token |
| Token Format | Base64 encoded credentials | JSON Web Token |
| State | Stateless (credentials each time) | Stateless (token contains info) |
| Security | Simple but less secure | More secure, can include expiration |
| Performance | Validates credentials each request | Validates token signature |

## Extending the Client

To add more functionality:

1. **Token Refresh**: Implement token refresh mechanism
2. **Token Storage**: Persist tokens between runs
3. **More Endpoints**: Test additional server endpoints
4. **Different Roles**: Test with custom roles
5. **Token Validation**: Add client-side token validation

## Related Projects

- **Lab12PartA**: Basic authentication server (Part 1)
- **Lab12PartC**: JWT authentication server (Part 3)
- **Lab12Client**: Basic auth client for Part 1