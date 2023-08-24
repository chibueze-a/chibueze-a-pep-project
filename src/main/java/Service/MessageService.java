package Service;

import Model.Message;
import DAO.MessageDAO;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage( int posted_by, String message_text, long time_posted_epoch){
        
    }
    
}
