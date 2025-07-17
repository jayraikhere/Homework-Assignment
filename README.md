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

[
    {
        "transactionId": 1,
        "customerId": 101,
        "billingPrice": 120,
        "billingDate": "2023-04-15T14:30:00"
    },
...
]
```

### Get Rewards for All Customers

```
GET /api/rewards
```

**Response Format:**

```json

[
    {
        "customerId": 101,
        "customerName": "John Doe",
        "totalReward": 190,
        "firstMonthReward": 90,
        "secondMonthReward": 50,
        "thirdMonthReward": 50
    },
    ...
]

```

### Get Rewards for Specific Customer

```
GET /api/rewards/customer/{customerId}
```

**Path Parameter:**

- `customerId`: ID of the customer (must be positive)

**Response Format:**

```json
[
    {
        "customerId": 101,
        "customerName": "John Doe",
        "totalReward": 190,
        "firstMonthReward": 90,
        "secondMonthReward": 50,
        "third MonthReward": 50
    }
    ...
]
```

**Error Response:**

```json
{
    "errorCode": 500,
    "errorMessage": "Something went wrong. Invalid customerId"
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