package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferHistory;

import java.util.List;

public interface TransferDao {


    void createTransfer(Transfer transfer);

    List<TransferHistory> getTransfersByUserId(int userId);

    Transfer getTransferByTransferId(int transferId);

    List<Transfer> getAllTransfers();

    void updateTransfer(Transfer transfer);
}
