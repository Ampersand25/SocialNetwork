package main.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryExceptionTest {
    private RepositoryException repositoryException;

    @BeforeEach
    void setUp() {
        repositoryException = new RepositoryException("Test");
    }

    @Test
    void testGetMessage() {
        assertEquals("Test", repositoryException.getMessage());
    }
}