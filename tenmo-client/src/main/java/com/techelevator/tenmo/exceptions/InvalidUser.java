package com.techelevator.tenmo.exceptions;

public class InvalidUser extends Exception {
    public InvalidUser() {
        super("Cannot send money to yourself. Please choose a different user.");
    }
 }

