package main.ui;

import main.domain.*;
import main.exceptions.RepositoryException;
import main.exceptions.ServiceException;
import main.exceptions.ValidationException;
import main.service.SuperService;

import java.io.IOException;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static main.utils.Constants.dateTimeFormatter;

public class UI {
    private final SuperService superService;

    /**
     * Constructor
     * @param superService the super service
     */
    public UI(SuperService superService) {
        this.superService = superService;
    }

    /**
     * Prints a menu of available commands
     */
    private static void printMenu() {
        System.out.println(
                """
                        MENU
                        1. Add user
                        2. Delete user
                        3. Display users
                        4. Add friendship
                        5. Delete friendship
                        6. Display friendships
                        7. Display number of communities
                        8. Display the most sociable community
                        9. Display friendships of a user
                        10. Display friendships of user from certain month
                        11. Messages submenu
                        12. Friend request submenu
                        0. Exit
                        """
        );
    }

    /**
     * Interacts with the user through a menu
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Enter command: ");
            String command = scanner.nextLine();
            try {
                switch (command) {
                    case "1":
                        addUser();
                        break;
                    case "2":
                        deleteUser();
                        break;
                    case "3":
                        displayUsers();
                        break;
                    case "4":
                        addFriendship();
                        break;
                    case "5":
                        deleteFriendship();
                        break;
                    case "6":
                        displayFriendships();
                        break;
                    case "7":
                        displayNoOfCommunities();
                        break;
                    case "8":
                        displayMostSociableCommunity();
                        break;
                    case "9":
                        displayFriendshipsOfUser();
                        break;
                    case "10":
                        displayFriendshipsOfUserFromMonth();
                        break;
                    case "11":
                        messagesSubmenu();
                        break;
                    case "12":
                        friendRequestSubmenu();
                        break;
                    case "0":
                        return;
                }
            }
            catch (RepositoryException | ValidationException | IOException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private static void printMessagesSubmenu() {
        System.out.println(
                """
                        MESSAGES SUBMENU
                        1. Send message
                        2. Reply message
                        3. Display conversation
                        0. Return to main menu
                        """
        );
    }

    private void messagesSubmenu() throws ValidationException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your user ID for authentication: ");
        Integer userID = Integer.parseInt(scanner.nextLine());

        User user = superService.getOneUser(userID);
        if (user == null)
            throw new ValidationException("This user ID does not exist.\n");

        System.out.println("Logged in as: " + superService.getOneUser(userID).getFirstName() + " " + superService.getOneUser(userID).getLastName() + "\n");

        while (true) {
            printMessagesSubmenu();
            System.out.print("Enter command: ");
            String command = scanner.nextLine();
            try {
                switch (command) {
                    case "1":
                        sendMessage(userID);
                        break;
                    case "2":
                        replyMessage(userID);
                        break;
                    case "3":
                        displayConversation(userID);
                        break;
                    case "0":
                        return;
                }
            } catch (ValidationException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private void replyMessage(Integer userID) {
        Scanner scanner = new Scanner(System.in);
    }

    private static void printFriendRequestSubmenu() {
        System.out.println(
                """
                        FRIEND REQUEST SUBMENU
                        1. Send friend request
                        2. Display friend requests
                        3. Accept friend request
                        4. Reject friend request
                        0. Return to main menu
                        """
        );
    }

    private void friendRequestSubmenu() throws ValidationException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your user ID for authentication: ");
        Integer userID = Integer.parseInt(scanner.nextLine());

        User user = superService.getOneUser(userID);
        if (user == null)
            throw new ValidationException("This user ID does not exist.\n");

        System.out.println("Logged in as: " + superService.getOneUser(userID).getFirstName() + " " + superService.getOneUser(userID).getLastName() + "\n");

        while (true) {
            printFriendRequestSubmenu();
            System.out.print("Enter command: ");
            String command = scanner.nextLine();
            try {
                switch (command) {
                    case "1":
                        sendFriendRequest(userID);
                        break;
                    case "2":
                        displayFriendRequestsForUser(userID);
                        break;
                    case "3":
                        approveFriendRequest(userID);
                        break;
                    case "4":
                        rejectFriendRequest(userID);
                        break;
                    case "0":
                        return;
                }
            } catch (ServiceException | RepositoryException | IOException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    /**
     * Read a user's first name and last name and adds the user to the repository
     * @throws ValidationException if user's attributes are not valid (null)
     * @throws RepositoryException if the user cannot be added to the repository
     * @throws IOException if the file cannot be found/opened
     */
    private void addUser() throws ValidationException, RepositoryException, IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter user's first name: ");
        String userFirstName = scanner.nextLine();
        System.out.print("Enter user's last name: ");
        String userLastName = scanner.nextLine();
        superService.addUser(userFirstName, userLastName);
        System.out.println("User added succesfully.\n");
    }

    /**
     * Read an ID and deletes from repository the user having that ID
     * @throws RepositoryException if there is no user having that ID in the repository
     * @throws IOException if the file cannot be found/opened
     */
    private void deleteUser() throws RepositoryException, IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter user's ID: ");
        Integer userID = Integer.parseInt(scanner.nextLine());
        superService.deleteUser(userID);
        System.out.println("User and user's friendships deleted succesfully.\n");
    }

    /**
     * Displays existing users in the repository
     */
    private void displayUsers() {
        Collection<User> users = superService.getAllUsers();
        if (users.isEmpty())
            System.out.println("There are no users in the repository.\n");
        else {
            System.out.println("Users:");
            for (User user : users)
                System.out.println(user.toString());
            System.out.println();
        }
    }

    /**
     * Read the necessary IDs and adds a friendship to the repository
     * @throws ValidationException if the friendship's attributes are not valid
     * @throws RepositoryException if the friendship cannot be added to the repository
     * @throws IOException if the file cannot be found/opened
     */
    private void addFriendship() throws ValidationException, RepositoryException, IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of first user: ");
        Integer firstUserID = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the ID of second user: ");
        Integer secondUserID = Integer.parseInt(scanner.nextLine());
        superService.addFriendship(firstUserID, secondUserID);
        System.out.println("Friendship added succesfully.\n");
    }

    /**
     * Read an ID and deletes from repository the friendship having that ID
     * @throws RepositoryException if there is no friendship having that ID in the repository
     * @throws IOException if the file cannot be found/opened
     */
    private void deleteFriendship() throws RepositoryException, IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of friendship: ");
        Integer friendshipID = Integer.parseInt(scanner.nextLine());
        superService.deleteFriendship(friendshipID);
        System.out.println("Friendship deleted succesfully.\n");
    }

    /**
     * Displays existing friendships in the repository
     */
    private void displayFriendships() {
        Collection<Friendship> friendships = superService.getAllFriendships();
        if (friendships.isEmpty())
            System.out.println("There are no friendships in the repository.\n");
        else {
            System.out.println("Friendships: ");
            for (Friendship friendship : friendships)
                System.out.println(friendship.toString());
            System.out.println();
        }
    }

    private void displayNoOfCommunities() {
        System.out.println("There are " + superService.countCommunities() + " communities in the social network!\n");
    }

    private void displayMostSociableCommunity() {
        List<Integer> list = superService.getMostActiveCommunity();
        int n = list.size();

        if(n == 0) {
            System.out.println("There are no existing communities in the social network!");
            return;
        }

        System.out.println("The most social community has " + n + " users, which are:");

        for(Integer id : list)
            //System.out.print(id + " ");
            System.out.println(superService.getOneUser(id));

        System.out.println("\n");
    }

    private void displayFriendshipsOfUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of user: ");
        Integer userID = Integer.parseInt(scanner.nextLine());

        List<Friendship> friendships = superService.getFriendshipsOfUser(userID);
        if (friendships.isEmpty())
            System.out.println("The user has no friends.\n");
        else {
            System.out.println("Friendships of user " + userID + ": ");
            for (Friendship friendship : friendships)
                if (friendship.getFirstUser().getID().equals(userID)) {
                    System.out.println("First name: " + friendship.getSecondUser().getFirstName() + " | Last name: " + friendship.getSecondUser().getLastName() + " | Date: " + friendship.getDate());
                }
                else {
                    System.out.println("First name: " + friendship.getFirstUser().getFirstName() + " | Last name: " + friendship.getFirstUser().getLastName() + " | Date: " + friendship.getDate());
                }
            System.out.println();
        }
    }

    private void displayFriendshipsOfUserFromMonth() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of user: ");
        Integer userID = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the month of the year: ");
        Integer monthID = Integer.parseInt(scanner.nextLine());

        try {
            Month month = superService.getMonthConst(monthID);
            User user = superService.getOneUser(userID);
            List<FriendshipByID> friendships = superService.getFriendshipsOfUserFromMonth(userID, monthID);

            if (friendships.isEmpty())
                System.out.println("User " + user.toString() + " doesn't have any friends in month " + month.toString() + "!");
            else {
                System.out.println("Friends of user " + user + " from month " + month + " of the year are:");

                for (FriendshipByID friendship : friendships)
                    System.out.println(friendship);
            }

            System.out.println();
        } catch (ValidationException | RepositoryException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void sendMessage(Integer from) throws ValidationException {
        Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter ID of sender: ");
        // Integer from = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter list of IDs of receivers: ");
        List<Integer> to = new ArrayList<>(Arrays.asList(scanner.nextLine().split(" ")))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.print("Enter message: ");
        String message = scanner.nextLine();

        superService.sendMessage(from, to, message);
    }

    private void displayConversation(Integer firstUserID) {
        Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter ID of first user: ");
        // Integer firstUserID = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter ID of second user: ");
        Integer secondUserID = Integer.parseInt(scanner.nextLine());

        System.out.println("Conversation between user " + firstUserID + " and " + secondUserID + ": ");
        List<Message> messages = superService.getConversation(firstUserID, secondUserID);
        for (Message message : messages) {
            String messageDateTime = message.getDateTime().format(dateTimeFormatter);
            String fromUser = message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
            String toUser = "";
            for (User user : message.getTo()) {
                if (user.getID().equals(firstUserID) || user.getID().equals(secondUserID))
                    toUser = user.getFirstName() + " " + user.getLastName();
            }
            String messageText = message.getMessage();
            System.out.printf("[%s] [ID: %s] From: %s to %s: %s%n", messageDateTime, message.getID(), fromUser, toUser, messageText);
        }
        System.out.println();
    }

    private void sendFriendRequest(Integer from) throws ValidationException, RepositoryException, IOException, ServiceException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ID of second user: ");
        Integer to = Integer.parseInt(scanner.nextLine());

        superService.addFriendRequest(from, to, "pending");
        System.out.println("Friend request sent successfully.\n");
    }

    private void approveFriendRequest(Integer userID) throws ValidationException, RepositoryException, ServiceException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ID of the request: ");
        Integer friendRequestID = Integer.parseInt(scanner.nextLine());

        superService.updateFriendRequest(userID, friendRequestID, "approved");
        System.out.println("Friend request approved successfully.\n");
    }

    private void rejectFriendRequest(Integer userID) throws ValidationException, RepositoryException, ServiceException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ID of the request: ");
        Integer friendRequestID = Integer.parseInt(scanner.nextLine());

        superService.updateFriendRequest(userID, friendRequestID, "rejected");
        System.out.println("Friend request rejected successfully.\n");
    }

    private void displayFriendRequestsForUser(Integer id) throws RepositoryException, ValidationException, ServiceException {
        List<FriendRequest> friendRequestList = superService.getFriendRequestsForUser(id);
        if (friendRequestList.isEmpty())
            System.out.println("There is no history of friendship requests for the given user.\n");
        else {
            System.out.println("Friendship requests history of the user:");
            for (FriendRequest friendRequest : friendRequestList)
                System.out.println(friendRequest);
            System.out.println();
        }
    }

    private void displayAllFriendRequests() throws RepositoryException {
        List<FriendRequest> friendRequestList = superService.getAllFriendRequests();
        if (friendRequestList.isEmpty())
            System.out.println("There are no friendship requests in the repository.\n");
        else {
            System.out.println("Friendship requests:");
            for (FriendRequest friendRequest : friendRequestList)
                System.out.println(friendRequest);
            System.out.println();
        }
    }
}
