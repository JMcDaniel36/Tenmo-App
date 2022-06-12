package com.techelevator.tenmo.model;

public class TransferStatus {
    private int transferStatusId;
    private int transferStatusDesc;

    public TransferStatus(int transferStatusId, int transferStatusDesc) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDesc = transferStatusDesc;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(int transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }
}
