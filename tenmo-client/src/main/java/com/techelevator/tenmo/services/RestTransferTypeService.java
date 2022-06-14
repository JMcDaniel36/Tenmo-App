package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferType;

public class RestTransferTypeService implements TransferTypeService{
    @Override
    public TransferType getTransferTypeByDesc(AuthenticatedUser authenticatedUser, String description) {
        return null;
    }

    @Override
    public TransferType getTransferTypeById(AuthenticatedUser authenticatedUser, int transferId) {
        return null;
    }
}
