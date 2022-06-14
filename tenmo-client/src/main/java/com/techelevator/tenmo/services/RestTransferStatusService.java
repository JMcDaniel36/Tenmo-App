package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;

public class RestTransferStatusService implements  TransferStatusService{
    @Override
    public TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, int transferStatusId) {
        return null;
    }

    @Override
    public TransferStatus getTransferStatusByDesc(AuthenticatedUser authenticatedUser, String description) {
        return null;
    }
}
