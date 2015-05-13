package com.hzflk.mime4j;

/**
 * A class to encapsulate MimeType related exceptions.
 */
public class MimeTypeException extends Exception {

    /**
     * Constructs a MimeTypeException with the specified detail message.
     * 
     * @param message the detail message.
     */
    public MimeTypeException(String message) {
        super(message);
    }

    /**
     * Constructs a MimeTypeException with the specified detail message
     * and root cause.
     * 
     * @param message the detail message.
     * @param cause root cause
     */
    public MimeTypeException(String message, Throwable cause) {
        super(message, cause);
    }

}