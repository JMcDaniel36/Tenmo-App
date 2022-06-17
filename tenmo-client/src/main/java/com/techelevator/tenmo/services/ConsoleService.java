package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static java.lang.System.out;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private Scanner in;


    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        out.println("*********************");
        out.println("* Welcome to Bank of Ghibli! *");
        out.println("*********************");
    }

    public void printLoginMenu() {
        out.println();
        out.println("1: Register");
        out.println("2: Login");
        out.println("0: Exit");
        out.println();
    }

    public void printMainMenu() {
        out.println();
        out.println("1: View your current balance");
        out.println("2: View your past transfers");
        out.println("3: View your pending requests");
        out.println("4: Send TE bucks");
        out.println("5: Request TE bucks");
        out.println("0: Exit");
        out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        out.println("An error occurred. Check the log for details.");
    }

    public int getUserInputInteger(String s) {
        Integer result = null;
        do {
            out.print(s +": ");
            out.flush();
            String userInput = in.nextLine();
            try {
                result = Integer.parseInt(userInput);
            } catch(NumberFormatException e) {
                out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
            }
        } while(result == null);
        return result;
    }

    public String getUserInput(String enter_amount) {
        return enter_amount;
    }

    public void printTransfers(int transferId, String fromOrTo, BigDecimal amount) {
        out.println(transferId + "     " + fromOrTo + "          " + "$ " + amount);
    }
    public void printTransferDetails(int id, String from, String to, String type, String status, BigDecimal amount) {
        out.println("-------------------------------");
        out.println("Transfer Details");
        out.println("-------------------------------");
        out.println("Id: " + id);
        out.println("From: " + from);
        out.println("To: " + to);
        out.println("Type: " + type);
        out.println("Status: " + status);
        out.println("Amount: $" + amount);
    }

    public void printUsers(User[] users) {
        for(User user: users) {
            out.println(user.getId() + "          " + user.getUsername());
        }
        out.println("---------");
        out.flush();
    }

    public void printTransferDetails(int transferId, String fromOrTo, BigDecimal amount) {
    }
}
