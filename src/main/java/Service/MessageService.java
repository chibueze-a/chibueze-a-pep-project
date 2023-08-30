package Service;

import Model.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageService {
    Message createMessage(Message message) throws SQLException;

    List<Message> getAllMessages() throws SQLException;

    Message getMessageById(int message_id) throws SQLException;

    void deleteMessage(int message_id) throws SQLException;

    Message updateMessage(int message_id, String newMessageText) throws SQLException;

    List<Message> getMessagesByUserId(int account_id) throws SQLException;
}
