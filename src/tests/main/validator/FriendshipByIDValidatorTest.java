package main.validator;

import main.domain.FriendshipByID;
import main.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipByIDValidatorTest {
    FriendshipByIDValidator friendshipByIDValidator;
    FriendshipByID friendship1, friendship2, friendship3, friendship4;

    @BeforeEach
    void setUp() {
        friendshipByIDValidator = new FriendshipByIDValidator();
        friendship1 = new FriendshipByID(1, 7, 8);
        friendship2 = new FriendshipByID(-1, 3, 9);
        friendship3 = new FriendshipByID(4, 3, -7);
        friendship4 = new FriendshipByID(-7, -4, -5);
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> friendshipByIDValidator.validate(friendship1));

        ValidationException exceptionThrown;
        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> friendshipByIDValidator.validate(friendship2)
        );
        assertEquals("ID of friendship is not valid.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> friendshipByIDValidator.validate(friendship3)
        );
        assertEquals("ID of second user is not valid.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                ValidationException.class,
                () -> friendshipByIDValidator.validate(friendship4)
        );
        assertEquals("ID of friendship is not valid.\nID of first user is not valid.\nID of second user is not valid.", exceptionThrown.getMessage());
    }
}