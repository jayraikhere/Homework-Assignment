# Retail Rewards System

A Spring Boot application that calculates customer rewards based on their purchase transactions. The reward calculation follows specific business rules, with points awarded based on transaction amounts.

## Tech Stack

- Java 17
- Spring Boot 3.4.7
- Spring Data JPA
- MySQL Database
- Maven

## Project Overview

This application manages retail customer transactions and calculates reward points based on the following rules:

- Purchases over $100 earn 2 points for every dollar spent over $100, plus 1 point for every dollar spent between $50 and $100

- Purchases between $50-$100 earn 1 point for every dollar spent over $50 

- Purchases under $50 earn no points

The system tracks rewards per customer on a monthly basis and provides a three-month total.

## Database Configuration

The application uses MySQL with the following configuration (`from src/main/resources/application.properties`):
```
spring.datasource.url=jdbc:mysql://localhost:3306/billing_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```
## API Endpoints

### Get All Transactions

```
GET /api/transactions
```
**Response Format:**

```json
{
    "success": true,
    "message": "Transactions retrieved successfully",
    "data": [
        {
            "transactionId": 101,
            "customerId": 1,
            "billingPrice": 5000,
            "billingDate": "2025-05-03T10:30:00"
        }
    ],
    "errors": null
}
```

### Get Rewards for Specific Customer

```
GET /api/rewards/customer/{customerId}?fromMonth={fromMonth}&toMonth={toMonth}
```

**Path Parameter:**
- `customerId`: ID of the customer (must be positive)

**Query Parameters:**
- `fromMonth` (optional): Starting month for rewards calculation (1-12)
- `toMonth` (optional): Ending month for rewards calculation (1-12)

**Validation Rules:**
- Month values must be between 1 and 12
- If provided, fromMonth must not be greater than toMonth
- Both parameters are optional. If omitted, rewards for all months will be calculated

**Success Response:**

```json
{
    "success": true,
    "message": "Rewards retrieved successfully for customer 1",
    "data": [
        {
            "customerId": 1,
            "customerName": "Aarav Mehta",
            "totalRewards": 9850,
            "monthlyRewards": {
                "MAY": 4850,
                "JUNE": 2550,
                "JULY": 2450
            }
        }
    ],
    "errors": null
}
```

**Error Response:**

```json
{
    "success": false,
    "message": "Customer ID must be greater than 0",
    "data": null,
    "errors": null
}
```

**Validation Error Response:**

```json
{
    "success": false,
    "message": "From month must be between 1 and 12; To month must be between 1 and 12",
    "data": null,
    "errors": null
}
```

## Data Models

### Customer

- `customerId` - Unique identifier
- `customerName` - Name of the customer
- `phone` - Contact number
- `email` - Email address

### Transaction

- `transactionId`-  Unique identifier
- `customer`-  Reference to Customer entity
- `billingPrice` -  Transaction amount
- `billingDate` - Date and time of the transaction

# Project Structure

- `controller` - REST API controllers
- `service` - Business logic implementation
- `repository` - Data access layer
- `entity` - JPA entity classes
- `dto` - Data transfer objects
- `exception`- Custom exception classes
- `utility` - Helper classes and exception handling