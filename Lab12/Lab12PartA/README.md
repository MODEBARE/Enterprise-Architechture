# Lab 12 - Spring Boot Role-Based Access Control

This Spring Boot application demonstrates role-based access control with three REST endpoints and two in-memory users.

## Project Structure

- `/shop` - Accessible by everyone (no authentication required)
- `/orders` - Accessible by all employees (requires EMPLOYEE role)
- `/payments` - Accessible only by finance department employees (requires FINANCE role)

## Users

- **Bob** (username: `bob`, password: `password`)
  - Roles: EMPLOYEE, SALES
  - Can access: `/shop` and `/orders`
  - Cannot access: `/payments`

- **Mary** (username: `mary`, password: `password`)
  - Roles: EMPLOYEE, FINANCE
  - Can access: `/shop`, `/orders`, and `/payments`

## Running the Application

1. Navigate to the project directory:
   ```bash
   cd Lab12
   ```

2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

3. The application will start on `http://localhost:8080`

## Testing with Postman

### 1. Test /shop endpoint (accessible by everyone)
- **Method**: GET
- **URL**: `http://localhost:8080/shop`
- **Authentication**: None required
- **Expected Result**: 200 OK with welcome message

### 2. Test /orders endpoint with Bob (sales employee)
- **Method**: GET
- **URL**: `http://localhost:8080/orders`
- **Authentication**: Basic Auth
  - Username: `bob`
  - Password: `password`
- **Expected Result**: 200 OK with orders message and user info

### 3. Test /orders endpoint with Mary (finance employee)
- **Method**: GET
- **URL**: `http://localhost:8080/orders`
- **Authentication**: Basic Auth
  - Username: `mary`
  - Password: `password`
- **Expected Result**: 200 OK with orders message and user info

### 4. Test /payments endpoint with Bob (should fail)
- **Method**: GET
- **URL**: `http://localhost:8080/payments`
- **Authentication**: Basic Auth
  - Username: `bob`
  - Password: `password`
- **Expected Result**: 403 Forbidden (Bob doesn't have FINANCE role)

### 5. Test /payments endpoint with Mary (should succeed)
- **Method**: GET
- **URL**: `http://localhost:8080/payments`
- **Authentication**: Basic Auth
  - Username: `mary`
  - Password: `password`
- **Expected Result**: 200 OK with payments message and user info

### 6. Test /payments endpoint without authentication (should fail)
- **Method**: GET
- **URL**: `http://localhost:8080/payments`
- **Authentication**: None
- **Expected Result**: 401 Unauthorized

## Security Configuration

The application uses Spring Security with:
- In-memory user details service
- BCrypt password encoding
- HTTP Basic authentication
- Role-based authorization rules

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Test
- Spring Security Test