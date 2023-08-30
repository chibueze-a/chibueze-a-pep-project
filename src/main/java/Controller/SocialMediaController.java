package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.AccountServiceImpl;
import Service.MessageService;
import Service.MessageServiceImpl;
import Util.ConnectionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {


    private AccountService accountService;
    private MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    public SocialMediaController() {
        this.accountService = new AccountServiceImpl(ConnectionUtil.getConnection());
        this.messageService = new MessageServiceImpl(ConnectionUtil.getConnection());
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);

        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);

        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUser(Context context) {
        try {
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account createdAccount = accountService.createAccount(account);
            context.json(createdAccount).status(200);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            context.status(400);
        }
    }

    private void loginUser(Context context) {
        try {
            String requestBody = context.body();
            if (requestBody == null || requestBody.isEmpty()) {
                context.status(400); // Bad Request
                return;
            }

            Account account = objectMapper.readValue(requestBody, Account.class);
            boolean loginSuccess = accountService.verifyLogin(account.getUsername(), account.getPassword());

            if (loginSuccess) {
                Account loggedInAccount = accountService.getAccountByUsername(account.getUsername());
                context.status(200).json(loggedInAccount); // OK
            } else {
                context.status(401); // Unauthorized
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            context.status(400); // Bad Request
        }
    }

    private void createMessage(Context context) {
        try {
            Message message = objectMapper.readValue(context.body(), Message.class);

            // Validate the message text length and user existence in the database
            if (message.getMessage_text().isEmpty()) {
                context.status(400); // Bad request for empty message text
                return;
            }

            if (message.getMessage_text().length() > 254) {
                context.status(400); // Bad request for message text too long
                return;
            }

            // Check if the user exists in the database 
            if (!userExists(message.getPosted_by())) {
                context.status(400); // Bad request for non-existing user
                return;
            }

            // Create the message
            Message createdMessage = messageService.createMessage(message);
            context.json(createdMessage).status(200); // Returns the newly created message

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            context.status(400); // Bad request for other errors
        }
    }

    private boolean userExists(int userId) throws SQLException {
        Account account = accountService.getAccountById(userId);
        return account != null;
    }



    private void getAllMessages(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
            context.json(messages);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500);
        }
    }

    private void getMessageById(Context context) throws SQLException {
        int messageId = Integer.parseInt(context.pathParam("{message_id}"));
        System.out.println(messageId);

        // Retrieve the message from your service using the provided messageId
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message); // Return the message as JSON response
            context.status(200);   // Set the status code to 200 (OK)
        } else {
            context.status(200);   // Set the status code to 200 because that is default
        }
    }


   /*  private void deleteMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("{message_id}"));
        try {
            messageService.deleteMessage(messageId);
            context.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(00);
        }
    }
*/

    private void deleteMessage(Context context) throws SQLException {
        int messageId = Integer.parseInt(context.pathParam("{message_id}"));
        Message deletedMessage = messageService.getMessageById(messageId);
        if (deletedMessage == null){
            context.status(200);
        }
        else {
            messageService.deleteMessage(messageId);
            context.status(200).json(deletedMessage);
        }
    }
    private void updateMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        String requestBody = context.body();
        try {
            JsonNode requestBodyJson = objectMapper.readTree(requestBody);
            String newMessageText = requestBodyJson.get("message_text").asText();

            if (newMessageText.length() >= 255 || newMessageText.isBlank()) {
                context.status(400);
                return;
            }

            messageService.updateMessage(messageId, newMessageText);

            Message updatedMessage = messageService.getMessageById(messageId);

            context.status(200).json(updatedMessage);
        } catch (IOException e) {
            e.printStackTrace();
            context.status(400);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
        }
    }


    private void getMessagesByUserId(Context context) {
        int accountId = Integer.parseInt(context.pathParam("{account_id}"));
        try {
            List<Message> messages = messageService.getMessagesByUserId(accountId);
            context.json(messages);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500);
        }
    }
    


}