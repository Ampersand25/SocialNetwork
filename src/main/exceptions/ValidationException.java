package main.exceptions;

/**
 * Own exception class
 */
public class ValidationException extends Exception {
    /** Constructor
     * @param message the message that will be associated with the exception
     */
    public ValidationException(String message) {
        super(message);
    }
}
