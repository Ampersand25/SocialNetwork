package main.domain;

import java.util.Objects;

public class User implements Entity<Integer> {
    private final int ID;
    private final String firstName;
    private final String lastName;

    /**
     * Constructor
     * @param ID user ID
     * @param firstName first name of the user
     * @param lastName last name of the user
     */
    public User(int ID, String firstName, String lastName) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Getter for user ID
     * @return user ID
     */
    @Override
    public Integer getID() {
        return ID;
    }

    /**
     * Getter for user's first name
     * @return user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for user's last name
     * @return user's last name
     */
    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID || (firstName.equals(user.firstName) && lastName.equals(user.lastName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, firstName, lastName);
    }
}
