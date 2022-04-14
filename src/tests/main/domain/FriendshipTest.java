package main.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipTest {
    private User user1, user2, user3, user4, user5;
    private Friendship friendship1, friendship2, friendship3, friendship4, friendship5;

    @BeforeEach
    void setUp() {
        user1 = new User(0, "George", "Tanase");
        user2 = new User(1, "Adrian", "Filip");
        user3 = new User(2, "Elon", "Musk");
        user4 = new User(3, "Andrei", "Tanase");
        user5 = new User(4, "Adrian", "Chelmus");
        friendship1 = new Friendship(0, user1, user2);
        friendship2 = new Friendship(1, user1, user4);
        friendship3 = new Friendship(2, user3, user5);
        friendship4 = new Friendship(3, user4, user1);
        friendship5 = new Friendship(0, user2, user5);
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
        assertEquals("Friendship{friendshipID=0, firstUser=User{ID=0, firstName='George', lastName='Tanase'}, secondUser=User{ID=1, firstName='Adrian', lastName='Filip'}}", friendship1.toString());
        assertEquals("Friendship{friendshipID=1, firstUser=User{ID=0, firstName='George', lastName='Tanase'}, secondUser=User{ID=3, firstName='Andrei', lastName='Tanase'}}", friendship2.toString());
        assertEquals("Friendship{friendshipID=2, firstUser=User{ID=2, firstName='Elon', lastName='Musk'}, secondUser=User{ID=4, firstName='Adrian', lastName='Chelmus'}}", friendship3.toString());
        assertEquals("Friendship{friendshipID=3, firstUser=User{ID=3, firstName='Andrei', lastName='Tanase'}, secondUser=User{ID=0, firstName='George', lastName='Tanase'}}", friendship4.toString());
        assertEquals("Friendship{friendshipID=0, firstUser=User{ID=1, firstName='Adrian', lastName='Filip'}, secondUser=User{ID=4, firstName='Adrian', lastName='Chelmus'}}", friendship5.toString());
    }
}