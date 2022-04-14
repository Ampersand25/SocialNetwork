package main.repository.database;

import main.domain.User;
import main.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDbRepository implements Repository<Integer, User> {
    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructor
     * @param url database URL, required to connect to the database
     * @param username database username
     * @param password database password
     */
    public UserDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Add a new user to the repository
     * @param entity the entity to be added to the repository
     */
    @Override
    public void save(User entity) {
        String sql_command = "INSERT INTO users (first_name, last_name) VALUES (?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for a user in the repository by ID
     * @param ID entity ID
     * @return the user, or null if there is no entity with that ID in the repository
     */
    @Override
    public User getOne(Integer ID) {
        String sql_command = "SELECT * FROM users WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
                ) {
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                return new User(ID, firstName, lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a collection of all users in the repository
     * @return a collection of all users in the repository
     */
    @Override
    public Iterable<User> getAll() {
        Collection<User> users = new ArrayList<>();
        String sql_command = "SELECT * FROM users";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = new User(ID, firstName, lastName);
                users.add(user);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        return users;
    }

    @Override
    public void update(User entity) {

    }

    /**
     * Delete a user from the repository, by its ID
     * @param ID the ID of the entity to be deleted
     */
    @Override
    public void delete(Integer ID) {
        String sql_command = "DELETE FROM users WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.setInt(1, ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear the repository (deletes all users)
     */
    @Override
    public void clear() {
        String sql_command = "DELETE FROM users";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
