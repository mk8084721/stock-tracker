package com.mkdev.stock_tracker.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException(String symbol) {
        super("Symbol Already exists: "+symbol);
    }
}
