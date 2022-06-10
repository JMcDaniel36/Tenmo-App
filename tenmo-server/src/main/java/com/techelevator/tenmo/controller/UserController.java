package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    UserDao userDao;
    AccountDao transferDao;

//Autowire all of them?

    //createTransfer
    //getTransfersByUserId
    //getTransferByTransferId
    //getAllTransfers();
    //updateTransfer(Transfer transfer);
}
