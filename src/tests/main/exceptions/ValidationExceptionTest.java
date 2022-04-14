package main.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {
    private ValidationException validationException;

    @BeforeEach
    void setUp() {
        validationException = new ValidationException("Test");
    }

    @Test
    void testGetMessage() {
        assertEquals("Test", validationException.getMessage());
    }
}