package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

public class MessageDAOUtil implements MessageDAO {

    @Override
    public Message createMessage(int posted_by, String message_text, long time_posted_epoch){

        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement statement = null; 

        try{
            statement = connection.prepareStatement(sql);
            statement.setInt(1, posted_by);
            statement.setString(2, message_text);
            statement.setLong(3, time_posted_epoch);

            int rowData = statement.executeUpdate();

            if (rowData > 0){
                return new Message(posted_by, message_text, time_posted_epoch);
            }


        }

        catch (SQLException e){
            e.printStackTrace();
        }
        finally{
            if (statement != null){
                try {
                    statement.close();

                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM message";
        PreparedStatement statement = null;
        ResultSet resultSet = null; 

        try{            

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while(resultSet.next()){

                int message_id = resultSet.getInt("message_id");
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");

               
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }

            resultSet.close();
        }
        catch (SQLException e ){
            e.printStackTrace();
        }
        finally{
            try{
                if ( resultSet != null){
                    resultSet.close();

                }

                if (statement != null){
                    statement.close();
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }   return messages;
    }

    @Override
    public Message getMessageById( int messageId){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            resultSet = statement.executeQuery();

            if (resultSet.next()){
                int message_id = resultSet.getInt("message_id");
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                return new Message(message_id, posted_by, message_text, time_posted_epoch);

            }
            else {
                return null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        finally{
            if (resultSet != null){
                try{
                    resultSet.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (statement != null){
                try {
                    statement.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean deleteMessage(int message_id) {
    
        Connection connection = ConnectionUtil.getConnection();

        //String selectSql = "SELECT * FROM message WHERE message_id = ?";

        String sql = "DELETE FROM message WHERE message_id = ?";

        PreparedStatement statement = null;
        //PreparedStatement selectStatement = null;

    
        try {
    
            //selectStatement = connection.prepareStatement(selectSql);
            statement = connection.prepareStatement(sql);
            statement.setInt(1, message_id);
            int rowData = statement.executeUpdate();
            return rowData > 0;
    
           
        } 
          catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    
          finally {
            if (statement != null) {
                try {
                    statement.close();
                } 
              catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } 
                catch (SQLException e) {

                    e.printStackTrace();
                }
            }
            
            
        }
    
        
    } 
    
}
