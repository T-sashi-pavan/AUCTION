package com.auction.exceptions;

/**
 * Custom exception for authentication failures
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
