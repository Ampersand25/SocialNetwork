package main.repository.file;

import main.domain.User;
import main.exceptions.RepositoryException;

import java.io.IOException;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Integer, User> {
    /**
     * Constructor
     * @param fileName the name of the file that contains the users
     * @throws IOException if the file cannot be found/opened
     * @throws RepositoryException if the users in the file cannot compose a valid repository
     */
    public UserFileRepository(String fileName) throws IOException, RepositoryException {
        super(fileName);
    }

    /**
     * Converts a string to a user
     * @param attributes the list of substrings to be transformed into a user
     * @return the user corresponding to the string
     */
    @Override
    public User extractEntity(List<String> attributes) {
        return new User(Integer.parseInt(attributes.get(0)), attributes.get(1), attributes.get(2));
    }

    /**
     * Converts a user to a string
     * @param user the user to be transformed into a string
     * @return the string corresponding to the user
     */
    @Override
    public String createEntityAsString(User user) {
        return user.getID().toString() + ';' + user.getFirstName() + ';' + user.getLastName();
    }
}
