package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.AccountServiceImpl;

import Service.MessageService;
import Service.MessageServiceImpl;

import java.io.IOException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import DAO.MessageDAOUtil;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;
    //private final AccountDAO accountDAO;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/:message_id", this::getMessageByIdHandler);
        app.delete("/messages/:message_id", this::deleteMessageHandler);
        app.patch("/messages/:message_id", this::updateMessageHandler);
        app.get("/accounts/:account_id/messages", this::getAllMessagesFromUserHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //private void exampleHandler(Context context) {
    //    context.json("sample text");
    //}

    private void registerHandler(Context context ){
        String requestBody = context.body();
        Account potentialAcc = jsonData(requestBody);

        if (isValidAcc(potentialAcc)){
            if(!accountService.existingAcc(potentialAcc.getUsername())){
                Account createdAcc = accountService.createAcc(potentialAcc);
                context.json(createdAcc).status(200);
            }
            else{
                context.result("Try again. Username is in use. ").status(400);
            }
        }
        else{
            context.result("Error. Try again").status(400);

        }
    }

    private void loginHandler(Context context ){
        String requestBody = context.body();
        Account potentialAcc = jsonData(requestBody);

        if (isValidAcc(potentialAcc)){
            if(accountService.verifiedAcc(potentialAcc)){
                context.json(potentialAcc).status(200);
            }
            else{
                context.result("Error").status(401);
            }
        }
        else{
            context.result("Error.Try again").status(400);

        }
    }

   

  //  public SocialMediaController(AccountDAO accountDAO){
  //      this.accountDAO = accountDAO;
  //  }

    private Account jsonData(String json){
        
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Account.class);

        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    private Account createAcc( Account potentialAcc){
       return accountService.createAcc(potentialAcc);
    }

    private boolean isValidAcc(Account potentialAcc){
        
        if (potentialAcc.getUsername() == null || potentialAcc.getUsername().isEmpty()){
            return false; //base Case (user should not be blank)
        }

        if(potentialAcc.getPassword() == null || potentialAcc.getPassword().length() < 4){
            return false; //password is at least 4 characters long
        }
        return true;


    }

    private boolean existingAcc(String username){
        
        return accountService.existingAcc(username);
    }

    private boolean verifiedAcc(Account potentialAcc){
        return accountService.verifiedAcc(potentialAcc);

    }

    private MessageDAO messageDAO;

    

    private void getAllMessagesHandler(Context context){
        List<Message> messages = messageDAO.getAllMessages();
        context.json(messages);
    }

    private void getMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        MessageDAO messageDAO = new MessageDAOUtil();
        Message message = messageDAO.getMessageById(messageId);

        if (message != null){
            context.json(message).status(200);
        }
        else{
            context.result("No message returned").status(200);
        }
    }

    private void deleteMessageHandler (Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));

        if (messageDAO.deleteMessage(messageId)){

            Message deletedMessage = new Message();
            context.json(deletedMessage).status(200);
        }

        else{
            context.result("").status(200);
        }
    }

    private void updateMessageHandler(Context context){

        int messageId = Integer.parseInt(context.pathParam("message_id"));

        String requestBody = context.body();

        if (isValidMessageText(requestBody)) {
            if (messageDAO.updateMessage(messageId, requestBody)) {
                Message updatedMessage = messageDAO.getMessageById(messageId);
                context.json(updatedMessage).status(200);
            } else {
                context.status(400);
            }
        } else {
            context.status(400);
        }



    }

    private boolean isValidMessageText(String messageText) {
       
        return messageText != null && !messageText.isEmpty() && messageText.length() <= 255;
    }

    private void getAllMessagesFromUserHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageDAO.getAllMessagesFromUser(accountId);
        context.json(messages);
    }



}