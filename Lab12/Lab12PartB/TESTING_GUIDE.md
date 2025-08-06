# WebSecurityProject2 - Testing Guide

## Overview
This Spring Boot application demonstrates role-based access control with 5 endpoints and 4 users with different roles.

## Endpoints and Access Control

| Endpoint | Access Level | Required Role |
|----------|-------------|---------------|
| `/info` | Public | None (permitAll) |
| `/user` | User only | `user` |
| `/admin` | Admin only | `admin` |
| `/manager` | Manager only | `manager` |
| `/topmanager` | Top Manager only | `topmanager` |

## Users in Database

| Username | Password | Role |
|----------|----------|------|
| `user` | `user` | `user` |
| `admin` | `admin` | `admin` |
| `manager` | `manager` | `manager` |
| `topmanager` | `topmanager` | `topmanager` |

## Running the Application

1. **Start MySQL Server** (ensure it's running on localhost:3306)
2. **Create Database**:
   ```sql
   CREATE DATABASE eadb;
   ```
3. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```
4. The application will start on `http://localhost:8080`

## Testing with Postman

### 1. Test `/info` endpoint (Public Access)
- **Method**: GET
- **URL**: `http://localhost:8080/info`
- **Authentication**: None
- **Expected Result**: 200 OK with "info"

### 2. Test `/user` endpoint with user credentials
- **Method**: GET
- **URL**: `http://localhost:8080/user`
- **Authentication**: Basic Auth
  - Username: `user`
  - Password: `user`
- **Expected Result**: 200 OK with "user info"

### 3. Test `/admin` endpoint with admin credentials
- **Method**: GET
- **URL**: `http://localhost:8080/admin`
- **Authentication**: Basic Auth
  - Username: `admin`
  - Password: `admin`
- **Expected Result**: 200 OK with "admin info"

### 4. Test `/manager` endpoint with manager credentials
- **Method**: GET
- **URL**: `http://localhost:8080/manager`
- **Authentication**: Basic Auth
  - Username: `manager`
  - Password: `manager`
- **Expected Result**: 200 OK with "manager info"

### 5. Test `/topmanager` endpoint with topmanager credentials
- **Method**: GET
- **URL**: `http://localhost:8080/topmanager`
- **Authentication**: Basic Auth
  - Username: `topmanager`
  - Password: `topmanager`
- **Expected Result**: 200 OK with "top manager info"

## Negative Testing (Should Fail)

### 1. Test `/admin` endpoint with user credentials (should fail)
- **Method**: GET
- **URL**: `http://localhost:8080/admin`
- **Authentication**: Basic Auth
  - Username: `user`
  - Password: `user`
- **Expected Result**: 403 Forbidden

### 2. Test `/manager` endpoint with user credentials (should fail)
- **Method**: GET
- **URL**: `http://localhost:8080/manager`
- **Authentication**: Basic Auth
  - Username: `user`
  - Password: `user`
- **Expected Result**: 403 Forbidden

### 3. Test `/topmanager` endpoint with manager credentials (should fail)
- **Method**: GET
- **URL**: `http://localhost:8080/topmanager`
- **Authentication**: Basic Auth
  - Username: `manager`
  - Password: `manager`
- **Expected Result**: 403 Forbidden

### 4. Test any protected endpoint without authentication (should fail)
- **Method**: GET
- **URL**: `http://localhost:8080/user` (or any protected endpoint)
- **Authentication**: None
- **Expected Result**: 401 Unauthorized

## Security Configuration Details

The role-based access control is configured in `SecurityConfig.java`:

```java
.requestMatchers(HttpMethod.GET, "/info").permitAll()
.requestMatchers(HttpMethod.GET, "/user").hasAuthority("user")
.requestMatchers(HttpMethod.GET, "/admin").hasAuthority("admin")
.requestMatchers(HttpMethod.GET, "/manager").hasAuthority("manager")
.requestMatchers(HttpMethod.GET, "/topmanager").hasAuthority("topmanager")
.anyRequest().authenticated()
```

## Database Schema

The application uses JPA with `create` DDL mode, so tables are automatically created:
- `User` table: stores username, password
- `Role` table: stores role information
- Relationship: One User can have multiple Roles (One-to-Many)

## Troubleshooting

1. **Database Connection Issues**: Ensure MySQL is running and database `eadb` exists
2. **403 Forbidden**: Check if user has the required role for the endpoint
3. **401 Unauthorized**: Ensure Basic Auth credentials are provided
4. **Application won't start**: Check if port 8080 is available

## Modified Files

The following files were modified to add the new endpoints and users:

1. **`MyController.java`**: Added `/manager` and `/topmanager` endpoints
2. **`Application.java`**: Added `manager` and `topmanager` users to database
3. **`SecurityConfig.java`**: Added authorization rules for new endpoints