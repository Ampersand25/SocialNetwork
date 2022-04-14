package main.exceptions;

/**
 * Own exception class
 */
public class RepositoryException extends Exception {
    /** Constructor
     * @param message the message that will be associated with the exception
     */
    public RepositoryException(String message) {
        super(message);
    }
}
