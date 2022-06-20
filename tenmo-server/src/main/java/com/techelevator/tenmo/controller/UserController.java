package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.model.Balance;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    UserDao userDao;
    TransferDao transferDao;
    TransferTypeDao transferTypeDao;
    TransferStatusDao transferStatusDao;
    AccountDao accountDao;

    public UserController(AccountDao AccountDao, TransferDao TransferDao, UserDao UserDao){
        this.accountDao = AccountDao;
        this.transferDao = TransferDao;
        this.userDao = UserDao;
    }

@RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal) {
        System.out.println(principal.getName());
        Balance balance = accountDao.getBalance(principal.getName());
        return balance;
    }

@RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUsers(){
    return userDao.findAll();
}

@ResponseStatus(HttpStatus.CREATED)
@RequestMapping(path="/transfers/{id}", method = RequestMethod.POST)
public void addTransfer(@RequestBody Transfer transfer, @PathVariable int id)  {

    Account accountFrom = accountDao.getAccountByUserID(transfer.getAccountFrom());
    Account accountTo = accountDao.getAccountByUserID(transfer.getAccountTo());

    if(accountFrom.getBalance().getBalance().compareTo(transfer.getAmount()) > 0 ) {
        transfer.setTransferStatusId(2);
    } else {
        transfer.setTransferStatusId(3);
    }

    accountFrom.getBalance().sendMoney(transfer.getAmount());
    accountTo.getBalance().receiveMoney(transfer.getAmount());

    transfer.setAccountFrom(accountFrom.getAccountId());
    transfer.setAccountTo(accountTo.getAccountId());

    transferDao.createTransfer(transfer);

    accountDao.updateAccount(accountFrom);
    accountDao.updateAccount(accountTo);
}

@RequestMapping(path="/transfertype/filter", method = RequestMethod.GET)
public TransferType getTransferTypeByDesc(@RequestParam String description) {
    return transferTypeDao.getTransferTypeByDesc(description);
}

@RequestMapping(path="/transferstatus/filter", method = RequestMethod.GET)
public TransferStatus getTransferStatusByDesc(@RequestParam String description) {
    return transferStatusDao.getTransferStatusByDesc(description);
}

@RequestMapping(path="/account/user/{id}", method = RequestMethod.GET)
public Account getAccountByUserId(@PathVariable int id) {
    return accountDao.getAccountByUserID(id);
}

@RequestMapping(path = "/account/{id}")
public Account getAccountByAccountId(@PathVariable int id) {
return accountDao.getAccountByAccountID(id);
}

@RequestMapping(path = "/transfers/user/{userId}", method = RequestMethod.GET)
public List<TransferHistory> getTransferByUserId(@PathVariable int userId){
    return  transferDao.getTransfersByUserId(userId);
}

@RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
public Transfer getTransferById (@PathVariable int id){
    return transferDao.getTransferByTransferId(id);
}

@RequestMapping(path = "/user/{username}" )
public User getUserByUsername(@PathVariable String username){
return userDao.findByUsername(username);
}
@RequestMapping(path = "/user/{id}" )
public int getUserById(@PathVariable String id){
    return userDao.findIdByUsername(id);
}

@RequestMapping(path="/transfers", method = RequestMethod.GET)
public List<Transfer> getAllTransfers() {
    return transferDao.getAllTransfers();
}

@RequestMapping(path="/transfertype/{id}", method = RequestMethod.GET)
public TransferType getTransferDescById(@PathVariable int id)  {
    return transferTypeDao.getTransferTypeById(id);
}

@RequestMapping(path="/transferstatus/{id}", method = RequestMethod.GET)
public TransferStatus getTransferStatusById(@PathVariable int id) {
    return transferStatusDao.getTransferStatusById(id);
}

@RequestMapping(path="/transfers/{id}", method = RequestMethod.PUT)
public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {

    // only go through with the transfer if it is approved
    if (transfer.getTransferStatusId() == transferStatusDao.getTransferStatusByDesc("Approved").getTransferStatusId()) {

        BigDecimal amountToTransfer = transfer.getAmount();
        Account accountFrom = accountDao.getAccountByAccountID(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByAccountID(transfer.getAccountTo());

        accountFrom.getBalance().sendMoney(amountToTransfer);
        accountTo.getBalance().receiveMoney(amountToTransfer);

        transferDao.updateTransfer(transfer);

        accountDao.updateAccount(accountFrom);
        accountDao.updateAccount(accountTo);
    } else {
        transferDao.updateTransfer(transfer);
    }
}

}
