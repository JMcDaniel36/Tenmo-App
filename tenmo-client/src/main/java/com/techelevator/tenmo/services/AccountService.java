package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;

public interface AccountService {

    Balance getBalance(AuthenticatedUser authenticatedUser) throws Exception;

    Account getAccountByUserId(AuthenticatedUser authenticatedUser, Long userId);

    Account getAccountById(AuthenticatedUser authenticatedUser, int accountId);


}
