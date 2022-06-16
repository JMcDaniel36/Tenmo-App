package com.techelevator.tenmo.exceptions;

public class InvalidTransferID extends Exception {
    public InvalidTransferID() {
        super("Invalid Transfer Id, please choose another Id.");
    }
}

