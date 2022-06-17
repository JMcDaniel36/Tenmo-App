package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticatedUser authenticatedUser;
    private AccountService accountService;
    private UserService userService;
    private TransferTypeService transferTypeService;
    private TransferStatusService transferStatusService;
    private TransferService transferService;

    public App(ConsoleService console, AuthenticationService authenticationService) {
        this.console = console;
        this.accountService = new RestAccountServices(API_BASE_URL);
        this.userService = new RestUserService();
        this.transferTypeService = new RestTransferTypeService(API_BASE_URL);
        this.transferStatusService = new RestTransferStatusService(API_BASE_URL);
        this.transferService = new RestTransferService(API_BASE_URL);
    }

    public App() {

    }


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }


    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private boolean validateUserChoice(int userIdChoice, User[] users, AuthenticatedUser currentUser) {
        if(userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;

                for (User user : users) {
                    if(userIdChoice == currentUser.getUser().getId()) {
                        throw new Exception();
                    }
                    if (user.getId() == userIdChoice) {
                        validUserIdChoice = true;
                        break;
                    }
                }
                if (validUserIdChoice == false) {
                    throw new Exception();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void printUserOptions(AuthenticatedUser currentUser, User[] users) {
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        Balance balance = accountService.getBalance(authenticatedUser);
        System.out.println("Your current balance is" + balance.getBalance());
    }

    private void viewTransferHistory() {
        Transfer[] transfers = transferService.getTransfersByUserId(authenticatedUser, Math.toIntExact(currentUser.getUser().getId()));
        System.out.println("-------------------------------");
        System.out.println("Transfers");
        System.out.println("ID     From/To          Amount");
        System.out.println("-------------------------------");

        int currentUserAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        for(Transfer transfer: transfers) {
            printTransferDetails(currentUser, transfer);
        }

        int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to view details (0 to cancel)");
        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if(transferChoice != null) {
            printTransferDetails(currentUser, transferChoice);
        }
    }

    private void viewPendingRequests() {
        System.out.println("We did not do pending requests. Sorry.");

    }

    private void sendBucks() {
        User[] users = userService.findAll(currentUser);
        printUserOptions(currentUser, users);

        int userIdChoice = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            transferService.createTransfer();
        }

    }
    private void requestBucks() {
        User[] users = userService.findAll(currentUser);
        printUserOptions(currentUser, users);
        int userIdChoice = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            transferService.createTransfer();
        }
    }

    private void printTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        String fromOrTo = "";
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        if (accountService.getAccountById(currentUser, accountTo).getUserId() == authenticatedUser.getUser().getId()) {
            int accountFromUserId = accountService.getAccountById(currentUser, accountFrom).getUserId();
            String userFromName = userService.findIdByUsername(currentUser, String.valueOf(accountFrom)).getUsername();
            fromOrTo = "From: " + userFromName;
        } else {
            int accountToUserId = accountService.getAccountById(currentUser, accountTo).getUserId();
            String userToName = userService.findIdByUsername(currentUser, String.valueOf(accountTo)).getUsername();
            fromOrTo = "To: " + userToName;
        }

       console.printTransferDetails(transfer.getTransferId(), fromOrTo, transfer.getAmount());

    }
    private void printTransferDetails(AuthenticatedUser currentUser, Transfer transferChoice) {
        int id = transferChoice.getTransferId();
        BigDecimal amount = transferChoice.getAmount();
        int fromAccount = transferChoice.getAccountFrom();
        int toAccount = transferChoice.getAccountTo();
        int transactionTypeId = transferChoice.getTransferTypeId();
        int transactionStatusId = transferChoice.getTransferStatusId();

        int fromUserId = accountService.getAccountById(currentUser, fromAccount).getUserId();
        String fromUserName = userService.findByUsername(currentUser, String.valueOf(fromUserId)).getUsername();
        int toUserId = accountService.getAccountById(currentUser, toAccount).getUserId();
        String toUserName = userService.findIdByUsername(currentUser, String.valueOf(toUserId)).getUsername();
        String transactionType = String.valueOf(transferTypeService.getTransferTypeById(currentUser,
                transactionTypeId).getTransferTypeDesc());
        String transactionStatus = String.valueOf(transferStatusService.getTransferStatusById(currentUser,
                transactionStatusId).getTransferStatusDesc());

        console.printTransferDetails(id, fromUserName, toUserName, transactionType, transactionStatus, amount);
    }

    private Transfer validateTransferIdChoice(int transferIdChoice, Transfer[] transfers, AuthenticatedUser currentUser) {
        Transfer transferChoice = null;
        if(transferIdChoice != 0) {
            try {
                boolean validTransferIdChoice = false;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferId() == transferIdChoice) {
                        validTransferIdChoice = true;
                        transferChoice = transfer;
                        break;
                    }
                }
                if (!validTransferIdChoice) {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transferChoice;
    }


    private void exitProgram () {
        System.exit(0);
    }
}
