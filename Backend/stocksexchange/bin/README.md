# Stock Exchange Application

## Project Overview
A comprehensive stock exchange application built with Spring Boot that provides functionality for trading stocks, managing portfolios, and handling user accounts. The application implements RESTful APIs for seamless integration and real-time trading operations.

## Features
- Account Management
- Portfolio Tracking
- Stock Trading (Buy/Sell)
- Real-time Stock Updates
- Trade History
- Multiple Portfolio Support
- Email Validation (Cognizant domain)

## Technology Stack
- **Framework**: Spring Boot 3.4.1
- **Build Tool**: Maven
- **Database**: MySQL
- **Java Version**: 21
- **Dependencies**:
  - Spring Data JPA
  - Spring Web
  - Spring Boot DevTools
  - Lombok
  - ModelMapper
  - Hibernate Validator
  - MySQL Connector

## Prerequisites
1. Java 21
2. Maven 3.6+
3. MySQL 8.0+
4. Git

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd stocksexchange
```

### 2. Database Configuration
Create `application.properties` in `src/main/resources`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/stockexchange
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080
```

### 3. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## API Documentation

### Account Management
#### Register New Trader
- **POST** `/api/stocktrader/new`
- **Request Body**:
```json
{
  "fname": "John",
  "lname": "Doe",
  "email": "john.doe@cognizant.com",
  "status": true
}
```
- **Note**: Email must be a valid Cognizant domain

### Portfolio Management
#### Get Account Portfolios
- **GET** `/api/portfolios/{accountId}`
- **Response**: List of portfolios

#### Update Portfolio Balance
- **PUT** `/api/portfolios/{portfolioId}/updatebalance`
- **Request Body**:
```json
{
  "newBalance": 1000.00
}
```

#### Add New Portfolio
- **POST** `/api/portfolios/{accountId}/new`
- **Request Body**:
```json
{
  "balance": 1000.00,
  "status": true
}
```

### Stock Management
#### Register New Stock
- **POST** `/api/stocks/companies/new`
- **Request Body**:
```json
{
  "name": "Example Corp",
  "symbol": "EXCP",
  "totalShares": 10000,
  "open": 50.00,
  "last": 51.00,
  "status": true,
  "type": "Equity"
}
```

#### Get Stocks by Type
- **GET** `/api/stocks/companies/type?type=Equity`

#### Update Stock Information
- **PUT** `/api/stocks/companies/{stockId}/update`

#### Update Stock Status
- **PATCH** `/api/stocks/{id}/status`

### Trading Operations
#### Market Buy Order
- **PUT** `/api/stock/buy/MarketPlan`
- **Request Body**:
```json
{
  "accountId": "DO1234",
  "symbol": "EXCP",
  "stockId": 1,
  "numShares": 100,
  "tradedAt": 50.00,
  "transType": "BUY",
  "typeofPurchase": "MARKET"
}
```

#### Market Sell Order
- **PUT** `/api/stock/sell/MarketPlan`
- **Request Body**: Similar to buy order with `transType`: "SELL"

## Data Models

### Account
- `accountId`: Auto-generated (2 letters from lastname + 4 random digits)
- `fname`: First name (min 3 characters)
- `lname`: Last name (min 3 characters)
- `email`: Cognizant email address
- `status`: Account status

### Portfolio
- `portfolioId`: UUID
- `accountId`: Reference to Account
- `balance`: Current balance
- `status`: Portfolio status

### Stock
- `stockId`: Integer
- `symbol`: Max 4 characters
- `name`: Company name
- `totalShares`: Available shares
- `open`: Opening price
- `last`: Last traded price
- `status`: Trading status
- `type`: Stock type

### Trade
- `tradeId`: Auto-generated
- `accountId`: Reference to Account
- `stockId`: Reference to Stock
- `numShares`: Number of shares
- `tradedAt`: Trade price
- `transType`: BUY/SELL
- `status`: Trade status

## Error Handling
The application implements global exception handling for:
- Invalid input validation
- Entity not found
- Business logic violations
- Database constraints

## Security Considerations
- Input validation on all endpoints
- Email domain restriction
- Balance validation
- Trade validation rules

## Testing
Run the test suite:
```bash
mvn test
```

## Monitoring
Health check endpoint:
- **GET** `/actuator/health`

