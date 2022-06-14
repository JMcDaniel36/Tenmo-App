package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferType;

public interface TransferTypeService {
    TransferType getTransferTypeByDesc(AuthenticatedUser authenticatedUser, String description);
    TransferType getTransferTypeById(AuthenticatedUser authenticatedUser, int transferId);

}
