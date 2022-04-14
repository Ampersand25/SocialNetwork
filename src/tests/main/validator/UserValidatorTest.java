package main.validator;

import main.domain.User;
import main.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    UserValidator userValidator;
    private User user1, user2, user3, user4, user5, user6;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
        user1 = new User(1, "Adrian", "Filip");
        user2 = new User(0, "Alex", "");
        user3 = new User(1000, "", "Tanase");
        user4 = new User(-8, "Andreea", "Grama");
        user5 = new User(5, "", "");
        user6 = new User(-1, "", "");
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> userValidator.validate(user1));

        ValidationException exceptionThrown;
        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(user2)
                );
        assertEquals("Last name is not valid.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(user3)
        );
        assertEquals("First name is not valid.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(user4)
        );
        assertEquals("ID is not valid.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(user5)
        );
        assertEquals("First name is not valid.\nLast name is not valid.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> userValidator.validate(user6)
        );
        assertEquals("ID is not valid.\nFirst name is not valid.\nLast name is not valid.", exceptionThrown.getMessage());
    }
}