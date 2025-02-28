# Trader Management System

## Overview
The Trader Management System is a Java-based application designed to manage trading accounts and trades. It provides functionalities to register and update accounts, as well as to manage trades over a specified period.

## Features
- Register new trading accounts.
- Update existing trading accounts.
- Retrieve trades from the last five days based on account and stock IDs.
- Save new trades into the system.

## API Endpoints

### 1. Register Account
- **Endpoint**: `POST /receiveAccount`
- **Request Body**: 
  ```json
  {
    "accountId": "string",
    "accountName": "string",
    "balance": "number"
  }
  ```
- **Response**: HTTP 200 OK

### 2. Update Account
- **Endpoint**: `POST /updateAccount`
- **Request Body**: 
  ```json
  {
    "accountId": "string",
    "accountName": "string",
    "balance": "number"
  }
  ```
- **Response**: HTTP 200 OK

### 3. Get Last Five Days Trades
- **Endpoint**: `GET /trades/pull`
- **Query Parameters**:
  - `accountId` (optional): The ID of the account.
  - `stockId` (optional): The ID of the stock.
- **Response**: 
  ```json
  [
    {
      "tradeId": "string",
      "accountId": "string",
      "stockId": "number",
      "tradeDate": "string",
      "quantity": "number",
      "price": "number"
    }
  ]
  ```

### 4. Receive Trade
- **Endpoint**: `POST /trades/receive`
- **Request Body**: 
  ```json
  {
    "tradeId": "string",
    "accountId": "string",
    "stockId": "number",
    "tradeDate": "string",
    "quantity": "number",
    "price": "number"
  }
  ```
- **Response**: A message indicating the result of the trade save operation.

## Project Description
The Trader Management System is designed to facilitate the management of trading accounts and trades. It allows users to register accounts, update account information, and manage trades efficiently.

## API Endpoints
To run the application, ensure you have Java and Maven installed. Use the following command to start the application:
```bash
./mvnw spring-boot:run
```

## Conclusion
This Trader Management System provides a robust solution for managing trading accounts and trades efficiently. For further details, refer to the API documentation or the source code.
