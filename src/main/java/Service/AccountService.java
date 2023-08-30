package Service;

import Model.Account;

import java.sql.SQLException;

public interface AccountService {
    Account createAccount(Account account) throws SQLException;

    Account getAccountByUsername(String username) throws SQLException;

    boolean verifyLogin(String username, String password);

    Account getAccountById(int accountId);
}
