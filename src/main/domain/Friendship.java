package main.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship implements Entity<Integer> {
    private final int friendshipID;
    private final User firstUser;
    private final User secondUser;
    private final LocalDate date;

    /**
     * Constructor
     * @param friendshipID friendship ID
     * @param firstUser one of the users of the friendship
     * @param secondUser the other user of the friendship
     * @param date the date when the users became friends
     */
    public Friendship(int friendshipID, User firstUser, User secondUser, LocalDate date) {
        this.friendshipID = friendshipID;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.date = date;
    }

    /**
     * Getter for entity ID
     * @return entity ID
     */
    public Integer getID() {
        return friendshipID;
    }

    /**
     * Getter for the first user of the friendship
     * @return first user of the friendship
     */
    public User getFirstUser() {
        return firstUser;
    }

    /**
     * Getter for the second user of the friendship
     * @return second user of the friendship
     */
    public User getSecondUser() {
        return secondUser;
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
        Friendship that = (Friendship) o;
        boolean same_order = firstUser.equals(that.firstUser) && secondUser.equals(that.secondUser);
        boolean reversed_order = firstUser.equals(that.secondUser) && secondUser.equals(that.firstUser);
        return friendshipID == that.friendshipID || same_order || reversed_order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendshipID, firstUser, secondUser);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "friendshipID=" + friendshipID +
                ", firstUser=" + firstUser +
                ", secondUser=" + secondUser +
                ", date=" + date +
                '}';
    }
}
