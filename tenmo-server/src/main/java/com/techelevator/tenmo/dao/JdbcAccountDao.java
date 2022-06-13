package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Balance getBalance(String user) {
        String sql = "SELECT balance FROM accounts " +
                "JOIN users ON accounts.user_id = users.user_id " +
                "WHERE username =?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        Balance balance = new Balance();

        if (results.next()) {
            String accountBalance = results.getString("Balance");
            assert accountBalance != null;
            balance.setBalance(new BigDecimal(accountBalance));
        }
        return balance;
    }

    @Override
    public Account getAccountByUserID(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if (result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountID(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts " +
                "WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if (result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public void updateAccount(Account accountToUpdate) {
        String sql = "UPDATE accounts " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccountId());
    }

//    private Account mapResultsToAccount(SqlRowSet result) {
//    }
}
