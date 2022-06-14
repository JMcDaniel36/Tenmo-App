package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

public class RestTransferService implements  TransferService{
    @Override
    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {

    }

    @Override
    public Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, int userId) {
        return new Transfer[0];
    }

    @Override
    public Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, int transferId) {
        return null;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        return new Transfer[0];
    }

    @Override
    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {

    }
}
