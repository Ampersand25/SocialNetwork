package main.repository.file;

import main.domain.FriendshipByID;
import main.exceptions.RepositoryException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Integer, FriendshipByID> {
    /**
     * Constructor
     * @param fileName the name of the file that contains the friendships
     * @throws IOException if the file cannot be found/opened
     * @throws RepositoryException if the friendships in the file cannot compose a valid repository
     */
    public FriendshipFileRepository(String fileName) throws IOException, RepositoryException {
        super(fileName);
    }

    /**
     * Converts a string to a friendship
     * @param attributes the list of substrings to be transformed into a friendship
     * @return the friendship corresponding to the string
     */
    @Override
    public FriendshipByID extractEntity(List<String> attributes) {
        return new FriendshipByID(Integer.parseInt(attributes.get(0)), Integer.parseInt(attributes.get(1)), Integer.parseInt(attributes.get(2)), LocalDate.parse(attributes.get(3)));
    }

    /**
     * Converts a friendship to a string
     * @param friendshipByID the friendship to be transformed into a string
     * @return the string corresponding to the friendship
     */
    @Override
    public String createEntityAsString(FriendshipByID friendshipByID) {
        return friendshipByID.getID().toString() + ';' + friendshipByID.getFirstUserID().toString() + ';' + friendshipByID.getSecondUserID().toString();
    }
}
