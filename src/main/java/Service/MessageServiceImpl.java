package Service;

import DAO.MessageDAO;
import DAO.MessageDAOImpl;
import Model.Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService {
    private MessageDAO messageDAO;

    public MessageServiceImpl(Connection connection) {
        this.messageDAO = new MessageDAOImpl(connection);
    }

    @Override
    public Message createMessage(Message message) throws SQLException {
        return messageDAO.createMessage(message);
    }

    @Override
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    @Override
    public Message getMessageById(int message_id) throws SQLException {
        return messageDAO.getMessageById(message_id);
    }

    @Override
    public void deleteMessage(int message_id) throws SQLException {
        messageDAO.deleteMessage(message_id);
    }

    @Override
    public Message updateMessage(int message_id, String newMessageText) throws SQLException {
        return messageDAO.updateMessage(message_id, newMessageText);
    }

    @Override
    public List<Message> getMessagesByUserId(int account_id) throws SQLException {
        return messageDAO.getMessagesByUserId(account_id);
    }
}
