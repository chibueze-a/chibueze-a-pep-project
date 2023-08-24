package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAO {

    Message createMessage ( int posted_by, String message_text, long time_posted_epoch);

    List<Message> getAllMessages();

    Message getMessageById(int message_id);
    
    boolean deleteMessage (int message_id);
    
}
