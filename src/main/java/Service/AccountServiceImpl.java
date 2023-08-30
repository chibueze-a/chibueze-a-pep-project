package Service;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import Model.Account;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountServiceImpl implements AccountService {
    private AccountDAO accountDAO;

    public AccountServiceImpl(Connection connection) {
        this.accountDAO = new AccountDAOImpl(connection);
    }

    @Override
    public Account createAccount(Account account) throws SQLException {
        return accountDAO.createAccount(account);
    }

    @Override
    public Account getAccountByUsername(String username) throws SQLException {
        return accountDAO.getAccountByUsername(username);
    }

    @Override
    public boolean verifyLogin(String username, String password) {
        try {
            Account account = accountDAO.getAccountByUsername(username);
            if (account != null) {
                if (account.getPassword().equals(password)) {
                    return true; // Login successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Login failed
    }

    @Override
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }


}
