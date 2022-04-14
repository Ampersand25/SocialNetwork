package main.repository;

import main.domain.User;
import main.exceptions.RepositoryException;
import main.repository.file.UserFileRepository;
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

class UserFileRepositoryTest {
    private UserFileRepository userFileRepository;
    private User user1, user2, user3, user4, user5;
    String fileName;

    private static void clearFileContent(String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
    }

    @BeforeEach
    void setUp() throws RepositoryException, IOException {
        fileName = "src/tests/main/data/users.csv";
        clearFileContent(fileName);
        userFileRepository = new UserFileRepository(fileName);
        user1 = new User(0, "George", "Tanase");
        user2 = new User(1, "Adrian", "Filip");
        user3 = new User(2, "Elon", "Musk");
        user4 = new User(0, "Andrei", "Tanase");
        user5 = new User(3, "Adrian", "Filip");
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        clearFileContent(fileName);
    }

    @Test
    void save() {
        assertDoesNotThrow(() -> userFileRepository.save(user1));
        assertDoesNotThrow(() -> userFileRepository.save(user2));
        assertDoesNotThrow(() -> userFileRepository.save(user3));

        RepositoryException exceptionThrown;
        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> userFileRepository.save(user4)
        );
        assertEquals("Entity with the same ID already exists in the repository.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> userFileRepository.save(user5)
        );
        assertEquals("This entity already exists in the repository.", exceptionThrown.getMessage());

        IllegalArgumentException illegalArgumentExceptionThrown = assertThrows(
                IllegalArgumentException.class,
                () -> userFileRepository.save(null)
        );
        assertEquals("Entity must be not null.", illegalArgumentExceptionThrown.getMessage());
    }

    @Test
    void delete() {
        IllegalArgumentException illegalArgumentExceptionThrown = assertThrows(
                IllegalArgumentException.class,
                () -> userFileRepository.delete(null)
        );
        assertEquals("ID must be not null.", illegalArgumentExceptionThrown.getMessage());

        save();
        assertDoesNotThrow(() -> userFileRepository.delete(1));

        RepositoryException exceptionThrown;
        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> userFileRepository.delete(1)
        );
        assertEquals("There is no entity having ID 1 in the repository.", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(
                RepositoryException.class,
                () -> userFileRepository.delete(3)
        );
        assertEquals("There is no entity having ID 3 in the repository.", exceptionThrown.getMessage());
    }

    @Test
    void getOne() {
        IllegalArgumentException exceptionThrown = assertThrows(
                IllegalArgumentException.class,
                () -> userFileRepository.getOne(null)
        );
        assertEquals("ID must be not null.", exceptionThrown.getMessage());

        save();
        assertEquals(user1, userFileRepository.getOne(0));
        assertEquals(user2, userFileRepository.getOne(1));
        assertEquals(user3, userFileRepository.getOne(2));
        assertNull(userFileRepository.getOne(3));
        assertNull(userFileRepository.getOne(-1));
    }

    @Test
    void getAll() {
        save();
        Collection<User> users = userFileRepository.getAll();
        assertEquals(3, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertTrue(users.contains(user3));
    }

    @Test
    void update() {
    }

    @Test
    void clear() {
        save();
        Collection<User> users = userFileRepository.getAll();
        assertEquals(3, users.size());
        userFileRepository.clear();
        users = userFileRepository.getAll();
        assertEquals(0, users.size());
    }

    @Test
    void extractEntity() {
        String[] parts = {"0", "George", "Tanase"};
        List<String> attributes = Arrays.asList(parts);
        User extractedUser = userFileRepository.extractEntity(attributes);
        assertEquals(0,extractedUser.getID());
        assertEquals("George",extractedUser.getFirstName());
        assertEquals("Tanase",extractedUser.getLastName());
    }

    @Test
    void createEntityAsString() {
        String stringFromEntity = userFileRepository.createEntityAsString(user1);
        assertEquals("0;George;Tanase", stringFromEntity);
    }
}