package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferHistory;

import java.util.List;

public interface TransferService {

    void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer);

    TransferHistory[] getTransfersByUserId(AuthenticatedUser authenticatedUser, int userId);

    Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, int transferId);

    Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser);

    void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer);
}
