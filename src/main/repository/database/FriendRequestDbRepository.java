package main.repository.database;

import main.domain.FriendRequestByID;
import main.exceptions.RepositoryException;
import main.repository.Repository;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepository implements Repository<Integer, FriendRequestByID> {
    private final String url;
    private final String username;
    private final String password;

    public FriendRequestDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void save(FriendRequestByID entity) throws RepositoryException, IOException {
        String sql_command = "SELECT COUNT(*) FROM friend_requests WHERE \"from\" = ? AND \"to\" = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.setInt(1, entity.getFrom());
            statement.setInt(2, entity.getTo());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer cont = resultSet.getInt(1);

                if(cont != 0)
                    throw new RepositoryException("Friend request already exists.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sql_command = "INSERT INTO friend_requests (\"from\", \"to\", \"date\", \"status\") VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.setInt(1, entity.getFrom());
            statement.setInt(2, entity.getTo());
            statement.setDate(3, java.sql.Date.valueOf(entity.getDate()));
            statement.setString(4, entity.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty() {
        String sql_command = "SELECT COUNT(*) FROM friend_requests";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Integer cont = resultSet.getInt(1);

                if (cont == 0)
                    return true;
            }
            else
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FriendRequestByID getOne(Integer ID) throws RepositoryException {
        if(isEmpty())
            throw new RepositoryException("There are no friend requests.\n");

        String sql_command = "SELECT * FROM friend_requests WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer from = resultSet.getInt("from");
                Integer to = resultSet.getInt("to");
                String status = resultSet.getString("status");
                LocalDate date = resultSet.getDate("date").toLocalDate();

                return new FriendRequestByID(ID, from, to, date, status);
            }
            else
                throw new RepositoryException("There is no friend request with the spicified id.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<FriendRequestByID> getAll() throws RepositoryException {
        List<FriendRequestByID> friendRequestByIDList = new ArrayList<>();
        String sql_command = "SELECT * FROM friend_requests";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            ResultSet resultSet = statement.executeQuery();
            boolean emptyRepo = true;
            while (resultSet.next()) {
                emptyRepo = false;

                Integer ID = resultSet.getInt("id");
                Integer from = resultSet.getInt("from");
                Integer to = resultSet.getInt("to");
                String status = resultSet.getString("status");
                LocalDate date = resultSet.getDate("date").toLocalDate();

                friendRequestByIDList.add(new FriendRequestByID(ID, from, to, date, status));
            }

            if (emptyRepo)
                throw new RepositoryException("There are no friend requests.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequestByIDList;
    }

    @Override
    public void update(FriendRequestByID friendRequestByID) throws RepositoryException {
        try {
            delete(friendRequestByID.getID());
            save(friendRequestByID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer ID) throws RepositoryException, IOException {
        String sql_command = "DELETE FROM friend_requests WHERE id = ?";
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

    @Override
    public void clear() {
        String sql_command = "DELETE FROM friend_requests";
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
