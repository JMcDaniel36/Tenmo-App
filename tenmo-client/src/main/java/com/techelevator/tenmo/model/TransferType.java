package com.techelevator.tenmo.model;

public class TransferType {
    private int transferTypeId;
    private int transferTypeDesc;

    public TransferType(int transferTypeId, int transferTypeDesc) {
        this.transferTypeId = transferTypeId;
        this.transferTypeDesc = transferTypeDesc;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(int transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }
}
