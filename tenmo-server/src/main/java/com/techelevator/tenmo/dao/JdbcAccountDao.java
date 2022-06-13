package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

public class JdbcAccountDao implements AccountDao{
    @Override
    public Balance getBalance(String user) {
        return null;
    }

    @Override
    public Account getAccountByUserID(int userId) {
        return null;
    }

    @Override
    public Account getAccountByAccountID(int accountId) {
        return null;
    }

    @Override
    public void updateAccount(Account accountToUpdate) {

    }
}
