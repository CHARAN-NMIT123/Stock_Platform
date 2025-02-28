package com.stocks.exceptions;

public class StockAlreadySoldException extends RuntimeException {
    public StockAlreadySoldException(String message) {
        super(message);
    }
}