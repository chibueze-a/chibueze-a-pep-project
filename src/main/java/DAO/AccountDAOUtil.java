package DAO;
import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAOUtil implements AccountDAO {
    @Override

    public Account createAcc( Account potentialAcc){
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement statement = null;

        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            //(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, potentialAcc.getUsername());
            statement.setString(2, potentialAcc.getPassword());

            int rowData = statement.executeUpdate();
            
            if (rowData > 0 ){
                return potentialAcc;
            }
            else{
                return null;
            }

        } 
        catch (SQLException e) {
            e.printStackTrace();

            //return null otherwise
            return null; 
        }
        finally{
            if (statement != null ){
                try {
                    statement.close();
                } 
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public boolean verifiedAcc(Account potentialAcc){
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;


        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password =?";

            // ( PreparedStatement statement = connection.prepareStatement(sql)){
            statement = connection.prepareStatement(sql);
            //user AND pass; if there exist a row then the account already exists 
            statement.setString(1, potentialAcc.getUsername());
            statement.setString(2, potentialAcc.getPassword());
            resultSet = statement.executeQuery();
            //try(ResultSet resultSet = statement.executeQuery()){
            return resultSet.next();
            //}
        }
        catch (SQLException e){

            e.printStackTrace();
            return false;
        }
        finally{
            if (resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }if ( statement != null){
                try{
                    statement.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
    }

    
    @Override
    public boolean existingAcc(String username){
        //same logic as above
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            //(PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
               
            
            if( resultSet.next()){
                int i = resultSet.getInt(1);
                return i > 0;
            }
            return false;

        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        finally{
            if (resultSet != null ){
                try{
                    resultSet.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (statement != null){
                try{
                    statement.close();
                }
                catch(SQLException e){
                    
                    e.printStackTrace();
                }
            }
        }
    }

    


}
