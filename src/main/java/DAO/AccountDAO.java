package DAO;

import Model.Account;

import java.sql.SQLException;

public interface AccountDAO {
    Account createAccount(Account account) throws SQLException;

    Account getAccountByUsername(String username) throws SQLException;

    Account getAccountById(int accountId);
}
