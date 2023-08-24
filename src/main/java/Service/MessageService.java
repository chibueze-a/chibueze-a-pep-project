package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public interface MessageService {

    Message createMessage( int posted_by, String message_text, long time_posted_epoch);
    List<Message>getAllMessages();
    Message getMessageById(int message_id);
    boolean deleteMessage (int message_id);
    boolean updateMessage(int message_id, String newMessageText);
    List<Message> getAllMessagesFromUser(int account_id);
    
        
    
    
}
