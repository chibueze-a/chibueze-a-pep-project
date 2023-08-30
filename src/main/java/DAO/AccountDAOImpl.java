package DAO;

import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAOImpl implements AccountDAO {
    private Connection connection;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Account createAccount(Account account) throws SQLException {
        if (account.getPassword().length() < 4)
            throw new SQLException("Password must be at least 4 characters long.");
        if (account.getUsername().isBlank())
            throw new SQLException("Username should not be blanked.");
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        }
        return account;
    }

    @Override
    public Account getAccountByUsername(String username) throws SQLException {
        String query = "SELECT * FROM account WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Account getAccountById(int accountId) {
        String query = "SELECT * FROM account WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
