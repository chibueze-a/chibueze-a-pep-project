package Service;

import Model.Message;
import Service.MessageService;
import DAO.MessageDAO;
import java.util.List;


public class MessageServiceImpl implements MessageService {

    private final MessageDAO messageDAO;

    public MessageServiceImpl(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    @Override
    public Message createMessage( int posted_by, String message_text, long time_posted_epoch){
        return messageDAO.createMessage(posted_by, message_text, time_posted_epoch);
    }

    @Override
    public List<Message>getAllMessages(){
        return messageDAO.getAllMessages();
    }

    @Override
    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);

    }

    @Override
    public boolean deleteMessage (int message_id){
        return messageDAO.deleteMessage(message_id);

    }

    @Override
    public boolean updateMessage(int message_id, String newMessageText){
        return updateMessage(message_id, newMessageText);

    }

    @Override
    public List<Message> getAllMessagesFromUser(int account_id){
        return messageDAO.getAllMessagesFromUser(account_id);
        
    }






}
