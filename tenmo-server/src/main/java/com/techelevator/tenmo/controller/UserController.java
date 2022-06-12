package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    UserDao userDao;
    TransferDao transferDao;

//Autowire all of them?

    //createTransfer
    //getTransfersByUserId
    //getTransferByTransferId
    //getAllTransfers();
    //updateTransfer(Transfer transfer);
}
