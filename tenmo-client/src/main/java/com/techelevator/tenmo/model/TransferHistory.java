package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferHistory {
    private int transferId;
    private int userFrom;
    private int userTo;
    private String toUsername;
    private String fromUsername;
    private BigDecimal amount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getUserFromId() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public int getUserToId() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}