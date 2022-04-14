package main.service;

import main.domain.*;
import main.exceptions.RepositoryException;
import main.exceptions.ServiceException;
import main.exceptions.ValidationException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SuperService {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;

    /**
     * Constructor
     * @param userService the service that deals with users
     * @param friendshipService the service that deals with friendships
     * @param friendRequestService
     */
    public SuperService(UserService userService, FriendshipService friendshipService, MessageService messageService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
    }

    /**
     * Adds a user
     * @param userFirstName user first name
     * @param userLastName user last name
     * @throws ValidationException if user's attributes are not valid
     */
    public void addUser(String userFirstName, String userLastName) throws ValidationException {
        userService.addUser(userFirstName, userLastName);
    }

    /**
     * Delete a user by its ID; also deletes all user's friendships
     * @param userID ID of the user to be deleted
     * @throws RepositoryException if there is no user having this ID in the repository
     * @throws IOException if the file cannot be found/opened
     */
    public void deleteUser(Integer userID) throws RepositoryException, IOException {
        friendshipService.deleteFriendshipsByUserID(userID);
        userService.deleteUser(userID);
    }

    /**
     * Gets a collection of all users in the repository
     * @return a collection of all users in the repository
     */
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Adds a friendship
     * @param firstUserID ID of one of the users
     * @param secondUserID the other user's ID
     * @throws ValidationException if friendship's attributes are not valid
     */
    public void addFriendship(Integer firstUserID, Integer secondUserID) throws ValidationException {
        friendshipService.addFriendship(firstUserID, secondUserID);
    }

    /**
     * Deletes a friendship
     * @param friendshipID ID of the friendship to be deleted
     * @throws RepositoryException if there is no friendship having this ID in the repository
     * @throws IOException if the file cannot be found/opened
     */
    public void deleteFriendship(Integer friendshipID) throws RepositoryException, IOException {
        friendshipService.deleteFriendship(friendshipID);
    }

    /**
     * Get a collection of all friendships (by ID) in the repository and converts them to real friendships (ID -> User by its ID)
     * @return a collection of all real friendships in the repository
     */
    public Collection<Friendship> getAllFriendships() {
        Collection<FriendshipByID> friendshipsByID = friendshipService.getAllFriendships();
        Map<Integer, Friendship> friendships = new HashMap<>();
        for (FriendshipByID friendshipByID : friendshipsByID) {
            int friendshipID = friendshipByID.getID();
            User firstUser = userService.getUser(friendshipByID.getFirstUserID());
            User secondUser = userService.getUser(friendshipByID.getSecondUserID());
            LocalDate date = friendshipByID.getDate();
            Friendship new_friendship = new Friendship(friendshipID, firstUser, secondUser, date);
            friendships.put(friendshipID, new_friendship);
        }
        return friendships.values();
    }

    public User getOneUser(Integer id) {
        return userService.getUser(id);
    }

    public Integer countCommunities() {
        return friendshipService.countCommunities();
    }

    public List<Integer> getMostActiveCommunity() {
        return friendshipService.getMostActiveCommunity();
    }

    public Month getMonthConst(Integer monthID) throws ValidationException {
        return friendshipService.getMonthConst(monthID);
    }

    public List<Friendship> getFriendshipsOfUser(Integer userID) {
        return getAllFriendships()
                .stream()
                .filter(f -> f.getFirstUser().getID().equals(userID) || f.getSecondUser().getID().equals(userID))
                .collect(Collectors.toList());
    }

    public List<FriendshipByID> getFriendshipsOfUserFromMonth(Integer userID, Integer monthID) throws RepositoryException, ValidationException {
        return friendshipService.getFriendshipsOfUserFromMonth(userID, monthID);
    }


    public void sendMessage(Integer from, List<Integer> to, String message) throws ValidationException {
        messageService.add(from, to, message);
    }

    public Message convertMessageByIDToMessage(MessageByID messageByID) {
        if (messageByID == null)
            return null;
        Integer messageID = messageByID.getID();
        User messageFrom = userService.getUser(messageByID.getFrom());
        List<User> messageTo = messageByID.getTo()
                .stream()
                .map(userService::getUser)
                .collect(Collectors.toList());
        LocalDateTime dateTime = messageByID.getDateTime();
        Message reply = null;
        if (messageByID.getReply() != null)
            reply = convertMessageByIDToMessage(messageService.getOne(messageByID.getReply()));
        return new Message(messageID, messageFrom, messageTo, messageByID.getMessage(), dateTime, reply);
    }

    public List<Message> getConversation(Integer firstUserID, Integer secondUserID) {
        List<MessageByID> messageByIDList = messageService.getConversation(firstUserID, secondUserID);
        List<Message> messages = new ArrayList<>();
        for (MessageByID messageByID : messageByIDList) {
            Message message = convertMessageByIDToMessage(messageByID);
            messages.add(message);
        }
        return messages;
    }

    private boolean checkForExistingFriendship(Integer userID1, Integer userID2) {
        Collection<FriendshipByID> friendshipList = friendshipService.getAllFriendships();

        if(friendshipList != null) {
            for(FriendshipByID friendshipByID : friendshipList) {
                Integer firstUserID = friendshipByID.getFirstUserID();
                Integer secondUserID = friendshipByID.getSecondUserID();

                if((firstUserID.equals(userID1) && secondUserID.equals(userID2)) || (firstUserID.equals(userID2) && secondUserID.equals(userID1)))
                    return true;
            }
        }

        return false;
    }

    public void addFriendRequest(Integer from, Integer to, String status) throws ValidationException, RepositoryException, IOException, ServiceException {
        if(userService.getUser(from) == null && userService.getUser(to) == null)
            throw new ServiceException("Sender and receiver users does not exist.\n");

        if(userService.getUser(from) == null)
            throw new ServiceException("Sender user does not exist.\n");

        if(userService.getUser(to) == null)
            throw new ServiceException("Receiver user does not exist.\n");

        if(checkForExistingFriendship(from, to))
            throw new ServiceException("Friendship already exists.\n");

        friendRequestService.add(from, to, status);
    }

    public FriendRequestByID getOneFriendRequest(Integer ID) throws RepositoryException {
        return friendRequestService.getOne(ID);
    }

    private List<FriendRequest> convertLists(List<FriendRequestByID> friendRequestByIDList) {
        if(friendRequestByIDList == null)
            return null;

        List<FriendRequest> friendRequestList = new ArrayList<>();

        for(FriendRequestByID friendRequestByID : friendRequestByIDList) {
            Integer id = friendRequestByID.getID();
            User from = userService.getUser(friendRequestByID.getFrom());
            User to = userService.getUser(friendRequestByID.getTo());
            LocalDate localDate = friendRequestByID.getDate();
            String status = friendRequestByID.getStatus();

            FriendRequest friendRequest = new FriendRequest(id, from, to, localDate, status);
            friendRequestList.add(friendRequest);
        }

        return friendRequestList;
    }

    public List<FriendRequest> getFriendRequestsForUser(Integer idUser) throws ValidationException, ServiceException, RepositoryException {
        if(idUser == null || idUser <= 0)
            throw new ValidationException("Invalid id of user.\n");

        if(userService.getUser(idUser) == null)
            throw new ServiceException("User does not exist.\n");

        return convertLists(friendRequestService.getAllUser(idUser));
    }

    public List<FriendRequest> getAllFriendRequests() throws RepositoryException {
        return convertLists(friendRequestService.getAll());
    }

    public void updateFriendRequest(Integer userID, Integer friendRequestID, String status) throws ValidationException, RepositoryException, ServiceException {
        FriendRequestByID friendRequestByID = friendRequestService.update(userID, friendRequestID, status);

        if(status.equalsIgnoreCase("approved")) {
            if(checkForExistingFriendship(friendRequestByID.getFrom(), friendRequestByID.getTo()))
                throw new ServiceException("Friendship already exists.\n");

            friendshipService.addFriendship(friendRequestByID.getFrom(), friendRequestByID.getTo());
        }
    }
}
