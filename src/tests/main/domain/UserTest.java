package main.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserTest {
    private User user1, user2, user3, user4, user5;

    @BeforeEach
    void setUp() {
        user1 = new User(0, "George", "Tanase");
        user2 = new User(1, "Adrian", "Filip");
        user3 = new User(2, "Elon", "Musk");
        user4 = new User(0, "Andrei", "Tanase");
        user5 = new User(0, "Adrian", "Filip");
    }

    @Test
    void getID() {
        assertEquals(0, user1.getID());
        assertEquals(1, user2.getID());
        assertEquals(2, user3.getID());
        assertEquals(0, user4.getID());
        assertEquals(0, user5.getID());
    }

    @Test
    void getFirstName() {
        assertEquals("George", user1.getFirstName());
        assertEquals("Adrian", user2.getFirstName());
        assertEquals("Elon", user3.getFirstName());
        assertEquals("Andrei", user4.getFirstName());
        assertEquals("Adrian", user5.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Tanase", user1.getLastName());
        assertEquals("Filip", user2.getLastName());
        assertEquals("Musk", user3.getLastName());
        assertEquals("Tanase", user4.getLastName());
        assertEquals("Filip", user5.getLastName());
    }

    @Test
    void testToString() {
        assertEquals("User{ID=0, firstName='George', lastName='Tanase'}", user1.toString());
        assertEquals("User{ID=1, firstName='Adrian', lastName='Filip'}", user2.toString());
        assertEquals("User{ID=2, firstName='Elon', lastName='Musk'}", user3.toString());
        assertEquals("User{ID=0, firstName='Andrei', lastName='Tanase'}", user4.toString());
        assertEquals("User{ID=0, firstName='Adrian', lastName='Filip'}", user5.toString());
    }

    @Test
    void testEquals() {
        assertNotEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1, user4);
        assertEquals(user1, user5);
        assertNotEquals(user2, user3);
        assertNotEquals(user2, user4);
        assertEquals(user2, user5);
        assertNotEquals(user3, user4);
        assertNotEquals(user3, user5);
        assertEquals(user4, user5);
    }

    @Test
    void testHashCode() {
        assertNotEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
        assertNotEquals(user1.hashCode(), user4.hashCode());
        assertNotEquals(user1.hashCode(), user5.hashCode());
        assertNotEquals(user2.hashCode(), user3.hashCode());
        assertNotEquals(user2.hashCode(), user4.hashCode());
        assertNotEquals(user2.hashCode(), user5.hashCode());
        assertNotEquals(user3.hashCode(), user4.hashCode());
        assertNotEquals(user3.hashCode(), user5.hashCode());
        assertNotEquals(user4.hashCode(), user5.hashCode());
    }
}