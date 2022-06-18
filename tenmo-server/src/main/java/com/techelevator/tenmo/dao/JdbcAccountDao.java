package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Balance getBalance(String user) {
        String sql = "SELECT account.balance FROM account " +
                "INNER JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        Balance balance = new Balance();

        if (results.next()) {
            String accountBalance = results.getString("balance");
            assert accountBalance != null;
            balance.setBalance(new BigDecimal(accountBalance));
        }
        return balance;
    }

    @Override
    public Account getAccountByUserID(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if (result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountID(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account " +
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
        String sql = "UPDATE account " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccountId());
    }

    private Account mapResultsToAccount(SqlRowSet result) {
        int accountId = result.getInt("account_id");
        int userAccountId = result.getInt("user_id");

        Balance balance = new Balance();
        String accountBalance = result.getString("balance");
        balance.setBalance(new BigDecimal(accountBalance));
        return new Account(accountId, userAccountId, balance);

    }
}

