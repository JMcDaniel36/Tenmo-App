package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusService {
    TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, int transferStatusId);
    TransferStatus getTransferStatusByDesc(AuthenticatedUser authenticatedUser, String description);

}
