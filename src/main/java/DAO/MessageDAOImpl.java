package DAO;

import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    private Connection connection;

    public MessageDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Message createMessage(Message message) throws SQLException {
        String query = "INSERT INTO message (posted_by, message_text) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }
        }
        return message;
    }

    @Override
    public List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                ));
            }
        }
        return messages;
    }

    @Override
    public Message getMessageById(int message_id) throws SQLException {
        String query = "SELECT * FROM message WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, message_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Message(
                            resultSet.getInt("message_id"),
                            resultSet.getInt("posted_by"),
                            resultSet.getString("message_text"),
                            resultSet.getLong("time_posted_epoch")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void deleteMessage(int message_id) throws SQLException {
        String query = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, message_id);
            statement.executeUpdate();
        }
    }

    @Override
    public Message updateMessage(int message_id, String newMessageText) throws SQLException {
        String query = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newMessageText);
            statement.setInt(2, message_id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating message failed, no rows affected.");
            }
        }
        return getMessageById(message_id);
    }

    @Override
    public List<Message> getMessagesByUserId(int account_id) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, account_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(new Message(
                            resultSet.getInt("message_id"),
                            resultSet.getInt("posted_by"),
                            resultSet.getString("message_text"),
                            resultSet.getLong("time_posted_epoch")
                    ));
                }
            }
        }
        return messages;
    }
}
