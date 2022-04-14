package main;

import main.exceptions.RepositoryException;
import main.repository.database.FriendshipDbRepository;
import main.repository.database.FriendRequestDbRepository;
import main.repository.database.MessageDbRepository;
import main.repository.database.UserDbRepository;
import main.service.*;
import main.ui.UI;
import main.utils.Constants;
import main.validator.FriendshipByIDValidator;
import main.validator.FriendRequestByIDValidator;
import main.validator.MessageByIDValidator;
import main.validator.UserValidator;

import java.io.IOException;

public class Main {

    // TODO de implementat cerintele cu grafuri
    // TODO de completat testele
    // TODO de finisat lucrul cu baza de date (validari)
    public static void main(String[] args) throws IOException, RepositoryException {
        UserDbRepository userRepository = new UserDbRepository(Constants.databaseURL, Constants.databaseUsername, Constants.databasePassword);
        UserValidator userValidator = new UserValidator();
        UserService userService = new UserService(userRepository, userValidator);
        FriendshipDbRepository friendshipRepository = new FriendshipDbRepository(Constants.databaseURL, Constants.databaseUsername, Constants.databasePassword);
        FriendshipByIDValidator friendshipByIDValidator = new FriendshipByIDValidator();
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository, friendshipByIDValidator);
        MessageDbRepository messageRepository = new MessageDbRepository(Constants.databaseURL, Constants.databaseUsername, Constants.databasePassword);
        MessageByIDValidator messageValidator = new MessageByIDValidator();
        MessageService messageService = new MessageService(messageRepository, messageValidator);
        FriendRequestByIDValidator friendRequestByIDValidator = new FriendRequestByIDValidator();
        FriendRequestDbRepository friendRequestDbRepository = new FriendRequestDbRepository(Constants.databaseURL, Constants.databaseUsername, Constants.databasePassword);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestDbRepository, friendRequestByIDValidator);
        SuperService superService = new SuperService(userService, friendshipService, messageService, friendRequestService);
        UI ui = new UI(superService);
        ui.run();
    }
}