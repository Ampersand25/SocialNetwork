package main.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Friendships, but the entities stored are not users; only the IDs of the users are stored
 * Instances of this class are stored in the repository and based on that, real Friendship objects are created on the service layer
 */
public class FriendshipByID implements Entity<Integer> {
    private final int ID;
    private final int firstUserID;
    private final int secondUserID;
    private final LocalDate date;

    /**
     * Constructor
     * @param ID friendship ID
     * @param firstUserID ID of first user of the friendship
     * @param secondUserID ID of second user of the friendship
     * @param date the date when the users became friends
     */
    public FriendshipByID(int ID, int firstUserID, int secondUserID, LocalDate date) {
        this.ID = ID;
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
        this.date = date;
    }

    /**
     * Getter for friendship ID
     * @return friendship ID
     */
    @Override
    public Integer getID() {
        return ID;
    }

    /**
     * Getter for the ID of first user
     * @return ID of first user
     */
    public Integer getFirstUserID() {
        return firstUserID;
    }

    /**
     * Getter for the ID of second user
     * @return ID of second user
     */
    public Integer getSecondUserID() {
        return secondUserID;
    }

    /**
     * Getter for date when the users became friends
     * @return date when the users became friends
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Checks if 2 friendships are equal
     * @param o the other friendship
     * @return True, if the friendships have the same ID or if the users of the friendships are the same
     *         False, otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipByID that = (FriendshipByID) o;
        boolean same_order = firstUserID == that.firstUserID && secondUserID == that.secondUserID;
        boolean reversed_order = firstUserID == that.secondUserID && secondUserID == that.firstUserID;
        return ID == that.ID || same_order || reversed_order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, firstUserID, secondUserID);
    }

    @Override
    public String toString() {
        return "FriendshipByID{" +
                "ID=" + ID +
                ", firstUserID=" + firstUserID +
                ", secondUserID=" + secondUserID +
                ", date=" + date +
                '}';
    }
}
