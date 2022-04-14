package main.repository.database;

import main.domain.MessageByID;
import main.exceptions.RepositoryException;
import main.repository.Repository;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static main.utils.Constants.dateTimeFormatter;

public class MessageDbRepository implements Repository<Integer, MessageByID> {
    private final String url;
    private final String username;
    private final String password;

    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void save(MessageByID entity) {
        String sql_command = "INSERT INTO messages (\"from\", message, date, reply) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
                ) {
            statement.setInt(1, entity.getFrom());
            statement.setString(2, entity.getMessage());
            statement.setString(3, entity.getDateTime().format(dateTimeFormatter));
            if (entity.getReply() != null)
                statement.setInt(4, entity.getReply());
            else
                statement.setNull(4, Types.INTEGER);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sql_command = "SELECT max(id) FROM messages";
        Integer messageID = 1;
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                messageID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        sql_command = "INSERT INTO messages_users (message_id, user_id) VALUES (?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
                ) {
            for(Integer userID : entity.getTo()) {
                statement.setInt(1, messageID);
                statement.setInt(2, userID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MessageByID getOne(Integer ID) {
        String sql_command = "SELECT * FROM messages WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int from = resultSet.getInt(2);
                String message = resultSet.getString(3);
                LocalDateTime date = LocalDateTime.parse(resultSet.getString(4), dateTimeFormatter);
                int reply = resultSet.getInt(5);
                List<Integer> to = new ArrayList<>();

                sql_command = "SELECT mu.user_id FROM messages INNER JOIN messages_users mu on messages.id = mu.message_id WHERE messages.id = ?";
                try (PreparedStatement statement2 = connection.prepareStatement(sql_command)) {
                    statement2.setInt(1, ID);
                    resultSet = statement2.executeQuery();
                    while(resultSet.next()) {
                        Integer receiverID = resultSet.getInt(1);
                        to.add(receiverID);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
                return new MessageByID(ID, from, to, message, date, reply);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<MessageByID> getAll() {
        List<MessageByID> messageByIDList = new ArrayList<>();
        String sql_command = "SELECT * FROM messages";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql_command)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int from = resultSet.getInt(2);
                String message = resultSet.getString(3);
                LocalDateTime date = LocalDateTime.parse(resultSet.getString(4), dateTimeFormatter);
                int reply = resultSet.getInt(5);
                List<Integer> to = new ArrayList<>();

                sql_command = "SELECT mu.user_id FROM messages INNER JOIN messages_users mu on messages.id = mu.message_id WHERE messages.id = ?";
                try (PreparedStatement statement2 = connection.prepareStatement(sql_command)) {
                    statement2.setInt(1, id);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while(resultSet2.next()) {
                        Integer receiverID = resultSet2.getInt(1);
                        to.add(receiverID);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
                messageByIDList.add(new MessageByID(id, from, to, message, date, reply));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageByIDList;
    }

    @Override
    public void update(MessageByID entity) {

    }

    @Override
    public void delete(Integer ID) throws RepositoryException, IOException {
        String sql_command = "DELETE FROM messages WHERE id = ?";
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
        String sql_command = "DELETE FROM messages";
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
