package main.service;

import main.domain.User;
import main.exceptions.RepositoryException;
import main.exceptions.ValidationException;
import main.repository.database.UserDbRepository;
import main.validator.UserValidator;

import java.util.Collection;

public class UserService {
    private final UserDbRepository userRepository;
    private final UserValidator userValidator;
    private int IDcounter;

    /**
     * Constructor
     * @param userRepository the repository that stores users
     * @param userValidator the validator of users
     */
    public UserService(UserDbRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.IDcounter = getMaxID() + 1;
    }

    /**
     * Gets the maximum ID of the users in the repository
     * @return maximum ID of the users in the repository
     */
    private Integer getMaxID() {
        Collection<User> users = (Collection<User>)userRepository.getAll();
        if (users.isEmpty())
            return -1;
        else {
            int maxID = 0;
            for (User user : users) {
                if (user.getID() > maxID)
                    maxID = user.getID();
            }
            return maxID;
        }
    }

    /**
     * Adds a user
     * @param userFirstName user first name
     * @param userLastName user last name
     * @throws ValidationException if user's attributes are not valid
     */
    public void addUser(String userFirstName, String userLastName) throws ValidationException {
        User user = new User(IDcounter, userFirstName, userLastName);
        userValidator.validate(user);
        userRepository.save(user);
        IDcounter++;
    }

    /**
     * Delete a user by its ID
     * @param userID ID of the user to be deleted
     * @throws RepositoryException if there is no user having this ID in the repository
     */
    public void deleteUser(Integer userID) throws RepositoryException {
        userRepository.delete(userID);
    }

    public User getUser(Integer userID) {
        return userRepository.getOne(userID);
    }

    /**
     * Gets a collection of all users in the repository
     * @return a collection of all users in the repository
     */
    public Collection<User> getAllUsers() {
        return (Collection<User>) userRepository.getAll();
    }
}
