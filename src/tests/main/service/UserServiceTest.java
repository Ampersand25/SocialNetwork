package main.service;

import main.domain.User;
import main.exceptions.RepositoryException;
import main.exceptions.ValidationException;
import main.repository.file.UserFileRepository;
import main.validator.UserValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserFileRepository userFileRepository;
    private UserValidator userValidator;
    private UserService userService;
    String fileName;

    private static void clearFileContent(String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
    }

    @BeforeEach
    void setUp() throws IOException, RepositoryException {
        fileName = "src/tests/main/data/users.csv";
        clearFileContent(fileName);
        userFileRepository = new UserFileRepository(fileName);
        userValidator = new UserValidator();
        userService = new UserService(userFileRepository, userValidator);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        clearFileContent(fileName);
    }

    @Test
    void addUser() {
        Collection<User> users = userService.getAllUsers();
        assertEquals(0, users.size());
        assertDoesNotThrow(() -> userService.addUser("George", "Tanase"));
        users = userService.getAllUsers();
        assertEquals(1, users.size());
        User user = (User) users.toArray()[0];
        assertEquals(0, user.getID());
        assertEquals("George", user.getFirstName());
        assertEquals("Tanase", user.getLastName());
        assertDoesNotThrow(() -> userService.addUser("Adrian", "Filip"));
        assertDoesNotThrow(() -> userService.addUser("Alex", "Brandabur"));
        assertDoesNotThrow(() -> userService.addUser("Catalin", "Chelmus"));

        ValidationException exceptionThrown = assertThrows(
                ValidationException.class,
                () -> userService.addUser("Alex", "")
        );
        assertEquals("Last name is not valid.", exceptionThrown.getMessage());
    }

    @Test
    void getAllUsers() {
        addUser();
        Collection<User> users = userService.getAllUsers();
        List<User> usersArray = new ArrayList<>(users);
        assertEquals(0, usersArray.get(0).getID());
        assertEquals(1, usersArray.get(1).getID());
        assertEquals(2, usersArray.get(2).getID());
        assertEquals(3, usersArray.get(3).getID());
    }

    @Test
    void deleteUser() throws RepositoryException, IOException {
        addUser();
        assertDoesNotThrow(() -> userService.deleteUser(1));
        assertDoesNotThrow(() -> userService.deleteUser(3));
        Collection<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }
}