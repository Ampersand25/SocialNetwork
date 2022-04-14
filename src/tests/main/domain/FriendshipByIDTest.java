package main.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipByIDTest {
    private FriendshipByID friendship1, friendship2, friendship3, friendship4, friendship5;

    @BeforeEach
    void setUp() {
        friendship1 = new FriendshipByID(0, 1, 2);
        friendship2 = new FriendshipByID(1, 1, 4);
        friendship3 = new FriendshipByID(2, 3, 5);
        friendship4 = new FriendshipByID(3, 4, 1);
        friendship5 = new FriendshipByID(0, 2, 5);
    }

    @Test
    void getID() {
        assertEquals(0, friendship1.getID());
        assertEquals(1, friendship2.getID());
        assertEquals(2, friendship3.getID());
        assertEquals(3, friendship4.getID());
        assertEquals(0, friendship5.getID());
    }

    @Test
    void getFirstUserID() {
        assertEquals(1, friendship1.getFirstUserID());
        assertEquals(1, friendship2.getFirstUserID());
        assertEquals(3, friendship3.getFirstUserID());
        assertEquals(4, friendship4.getFirstUserID());
        assertEquals(2, friendship5.getFirstUserID());
    }

    @Test
    void getSecondUserID() {
        assertEquals(2, friendship1.getSecondUserID());
        assertEquals(4, friendship2.getSecondUserID());
        assertEquals(5, friendship3.getSecondUserID());
        assertEquals(1, friendship4.getSecondUserID());
        assertEquals(5, friendship5.getSecondUserID());
    }

    @Test
    void testEquals() {
        assertNotEquals(friendship1, friendship2);
        assertNotEquals(friendship1, friendship3);
        assertNotEquals(friendship1, friendship4);
        assertEquals(friendship1, friendship5);
        assertNotEquals(friendship2, friendship3);
        assertEquals(friendship2, friendship4);
        assertNotEquals(friendship2, friendship5);
        assertNotEquals(friendship3, friendship4);
        assertNotEquals(friendship3, friendship5);
        assertNotEquals(friendship4, friendship5);
    }

    @Test
    void testHashCode() {
        assertNotEquals(friendship1.hashCode(), friendship2.hashCode());
        assertNotEquals(friendship1.hashCode(), friendship3.hashCode());
        assertNotEquals(friendship1.hashCode(), friendship4.hashCode());
        assertNotEquals(friendship1.hashCode(), friendship5.hashCode());
        assertNotEquals(friendship2.hashCode(), friendship3.hashCode());
        assertNotEquals(friendship2.hashCode(), friendship4.hashCode());
        assertNotEquals(friendship2.hashCode(), friendship5.hashCode());
        assertNotEquals(friendship3.hashCode(), friendship4.hashCode());
        assertNotEquals(friendship3.hashCode(), friendship5.hashCode());
        assertNotEquals(friendship4.hashCode(), friendship5.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Friendship{ID=0, firstUserID=1, secondUserID=2}", friendship1.toString());
        assertEquals("Friendship{ID=1, firstUserID=1, secondUserID=4}", friendship2.toString());
        assertEquals("Friendship{ID=2, firstUserID=3, secondUserID=5}", friendship3.toString());
        assertEquals("Friendship{ID=3, firstUserID=4, secondUserID=1}", friendship4.toString());
        assertEquals("Friendship{ID=0, firstUserID=2, secondUserID=5}", friendship5.toString());
    }
}