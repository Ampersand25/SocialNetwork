package main.repository.database;

import main.domain.FriendshipByID;
import main.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class FriendshipDbRepository implements Repository<Integer, FriendshipByID> {
    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructor
     * @param url database URL, required to connect to the database
     * @param username database username
     * @param password database password
     */
    public FriendshipDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Add a new friendship to the repository
     * @param entity the entity to be added to the repository
     */
    @Override
    public void save(FriendshipByID entity) {
        String sql_command = "INSERT INTO friendships (\"firstUserID\", \"secondUserID\", date) VALUES (?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
                ) {
            statement.setInt(1, entity.getFirstUserID());
            statement.setInt(2, entity.getSecondUserID());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for a friendship in the repository by ID
     * @param ID entity ID
     * @return the friendship, or null if there is no entity with that ID in the repository
     */
    @Override
    public FriendshipByID getOne(Integer ID) {
        String sql_command = "SELECT * FROM friendships WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
                ) {
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int firstUserID = resultSet.getInt("firstUserID");
                int secondUserID = resultSet.getInt("secondUserID");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));

                return new FriendshipByID(ID, firstUserID, secondUserID, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a collection of all friendships in the repository
     * @return a collection of all friendships in the repository
     */
    @Override
    public Iterable<FriendshipByID> getAll() {
        Collection<FriendshipByID> friendshipByIDCollection = new ArrayList<>();
        String sql_command = "SELECT * FROM friendships";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command);
                ResultSet resultSet = statement.executeQuery()
                ) {
            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                int firstUserID = resultSet.getInt("firstUserID");
                int secondUserID = resultSet.getInt("secondUserID");
                LocalDate date = LocalDate.parse(resultSet.getString("date"));

                FriendshipByID friendshipByID = new FriendshipByID(ID, firstUserID, secondUserID, date);
                friendshipByIDCollection.add(friendshipByID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendshipByIDCollection;
    }

    @Override
    public void update(FriendshipByID entity) {

    }

    /**
     * Delete a friendship from the repository, by its ID
     * @param ID the ID of the entity to be deleted
     */
    @Override
    public void delete(Integer ID) {
        String sql_command = "DELETE FROM friendships WHERE id = ?";
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
     * Clear the repository (delete all friendships)
     */
    @Override
    public void clear() {
        String sql_command = "DELETE FROM friendships";
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
