package com.stocks.exceptions;

public class AccountIdDoesNotOwnTheStockException extends RuntimeException {
    public AccountIdDoesNotOwnTheStockException(String message) {
        super(message);
    }
}
