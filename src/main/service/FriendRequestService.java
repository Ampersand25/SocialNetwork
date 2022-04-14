package main.service;

import main.domain.FriendRequestByID;
import main.exceptions.RepositoryException;
import main.exceptions.ServiceException;
import main.exceptions.ValidationException;
import main.repository.database.FriendRequestDbRepository;
import main.validator.FriendRequestByIDValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class FriendRequestService {
    private final FriendRequestDbRepository friendRequestDbRepository;
    private final FriendRequestByIDValidator friendRequestByIDValidator;

    public FriendRequestService(FriendRequestDbRepository friendRequestDbRepository, FriendRequestByIDValidator friendRequestByIDValidator) {
        this.friendRequestDbRepository = friendRequestDbRepository;
        this.friendRequestByIDValidator = friendRequestByIDValidator;
    }

    public void add(Integer from, Integer to, String status) throws ValidationException, RepositoryException, IOException {
        FriendRequestByID friendRequestByID = new FriendRequestByID(from, to, LocalDate.now(), status);
        friendRequestByIDValidator.validate(friendRequestByID);
        friendRequestDbRepository.save(friendRequestByID);
    }

    public FriendRequestByID getOne(Integer ID) throws RepositoryException {
        return friendRequestDbRepository.getOne(ID);
    }

    public List<FriendRequestByID> getAllUser(Integer idUser) throws RepositoryException {
        List<FriendRequestByID> friendRequestByIDList = (List<FriendRequestByID>) friendRequestDbRepository.getAll();
        return friendRequestByIDList
                .stream()
                .filter(friendshipRequest -> (friendshipRequest.getTo().equals(idUser) || friendshipRequest.getFrom().equals(idUser)))
                .toList();
    }

    public List<FriendRequestByID> getAll() throws RepositoryException {
        return (List<FriendRequestByID>) friendRequestDbRepository.getAll();
    }

    public FriendRequestByID update(Integer userID, Integer friendRequestID, String status) throws ValidationException, RepositoryException, ServiceException {
        if(friendRequestID == null || friendRequestID <= 0)
            throw new ValidationException("Invalid friend request id.\n");

        FriendRequestByID oldFriendRequestByID = friendRequestDbRepository.getOne(friendRequestID);

        if(!Objects.equals(oldFriendRequestByID.getTo(), userID))
            throw new ServiceException("Invalid friendship request!\n");

        if (!oldFriendRequestByID.getStatus().equalsIgnoreCase("pending"))
            throw new ServiceException("There is no pending friend request with the given id.\n");

        FriendRequestByID newFriendRequestByID = new FriendRequestByID(oldFriendRequestByID.getID(), oldFriendRequestByID.getFrom(), oldFriendRequestByID.getTo(), oldFriendRequestByID.getDate(), status);
        friendRequestByIDValidator.validate(newFriendRequestByID);
        friendRequestDbRepository.update(newFriendRequestByID);

        return oldFriendRequestByID;
    }
}
