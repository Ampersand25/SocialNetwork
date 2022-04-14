package main.repository;

import main.domain.FriendshipByID;
import main.exceptions.RepositoryException;
import main.repository.file.FriendshipFileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipFileRepositoryTest {
    private FriendshipFileRepository friendshipFileRepository;
    private FriendshipByID friendship1, friendship2, friendship3, friendship4, friendship5;
    String fileName;

    private static void clearFileContent(String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
    }

    @BeforeEach
    void setUp() throws RepositoryException, IOException {
        fileName = "src/tests/main/data/friendships.csv";
        clearFileContent(fileName);
        friendshipFileRepository = new FriendshipFileRepository(fileName);
        friendship1 = new FriendshipByID(0, 1, 2);
        friendship2 = new FriendshipByID(1, 1, 3);
        friendship3 = new FriendshipByID(2, 2, 5);
        friendship4 = new FriendshipByID(0, 3, 4);
        friendship5 = new FriendshipByID(3, 5, 2);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        clearFileContent(fileName);
    }

    @Test
    void save() {
        assertDoesNotThrow(() -> friendshipFileRepository.save(friendship1));
        assertDoesNotThrow(() -> friendshipFileRepository.save(friendship2));
        assertDoesNotThrow(() -> friendshipFileRepository.save(friendship3));

        RepositoryException exceptionThrown;
        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> friendshipFileRepository.save(friendship4)
        );
        assertEquals("Entity with the same ID already exists in the repository.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> friendshipFileRepository.save(friendship5)
        );
        assertEquals("This entity already exists in the repository.", exceptionThrown.getMessage());
    }

    @Test
    void delete() {
        save();
        assertDoesNotThrow(() -> friendshipFileRepository.delete(1));

        RepositoryException exceptionThrown;
        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> friendshipFileRepository.delete(1)
        );
        assertEquals("There is no entity having ID 1 in the repository.", exceptionThrown.getMessage());
    }

    @Test
    void getOne() {
        save();
        assertEquals(friendship1, friendshipFileRepository.getOne(0));
        assertEquals(friendship2, friendshipFileRepository.getOne(1));
        assertEquals(friendship3, friendshipFileRepository.getOne(2));
        assertNull(friendshipFileRepository.getOne(6));
        assertNull(friendshipFileRepository.getOne(-1));
    }

    @Test
    void getAll() {
        save();
        Collection<FriendshipByID> friendships = friendshipFileRepository.getAll();
        assertEquals(3, friendships.size());
    }

    @Test
    void update() {
    }

    @Test
    void clear() {
        save();
        Collection<FriendshipByID> friendships = friendshipFileRepository.getAll();
        assertEquals(3, friendships.size());
        friendshipFileRepository.clear();
        friendships = friendshipFileRepository.getAll();
        assertEquals(0, friendships.size());
    }

    @Test
    void extractEntity() {
        String[] parts = {"0", "1", "7"};
        List<String> attributes = Arrays.asList(parts);
        FriendshipByID extractedFriendship = friendshipFileRepository.extractEntity(attributes);
        assertEquals(0, extractedFriendship.getID());
        assertEquals(1, extractedFriendship.getFirstUserID());
        assertEquals(7, extractedFriendship.getSecondUserID());
    }

    @Test
    void createEntityAsString() {
        String stringFromEntity = friendshipFileRepository.createEntityAsString(friendship1);
        assertEquals("0;1;2", stringFromEntity);
    }
}