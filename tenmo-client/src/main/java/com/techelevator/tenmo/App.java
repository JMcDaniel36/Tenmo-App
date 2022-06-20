package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.sql.SQLOutput;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private int TransferStatusRequest = 1;
    private int TransferStatusSend = 2;
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AccountService accountService;
    private UserService userService;
    private TransferTypeService transferTypeService;
    private TransferStatusService transferStatusService;
    private TransferService transferService;

    public App() {
        this.accountService = new RestAccountServices(API_BASE_URL);
        this.userService = new RestUserService();
        this.transferTypeService = new RestTransferTypeService(API_BASE_URL);
        this.transferStatusService = new RestTransferStatusService(API_BASE_URL);
        this.transferService = new RestTransferService(API_BASE_URL);
        this.console = new ConsoleService();
    }


    public static void main(String[] args) throws Exception {
        App app = new App();
        app.run();
    }


    private void run() throws Exception {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private boolean validateUserChoice(int userIdChoice, User[] users, AuthenticatedUser currentUser) {
        if (userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;

                for (User user : users) {
                    if (userIdChoice == currentUser.getUser().getId()) {
                        System.out.println("You cannot send Totoro Bucks to yourself. ");
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
            System.out.println("Registration successful. You can now login to Bank of Ghibli!");
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

    private void mainMenu() throws Exception {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
//            } else if (menuSelection == 3) {
//                viewPendingRequests();
            } else if (menuSelection == 3) {
                sendBucks();
//            } else if (menuSelection == 5) {
//                requestBucks();
            } else if (menuSelection == 0) {
                System.out.println("Thank you for using Bank of Ghibli! See you again soon!");
                exitProgram();
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() throws Exception {
        Balance balance = accountService.getBalance(currentUser);
        System.out.println("Your current balance is $" + balance.getBalance());
    }

    private void viewTransferHistory() {
        TransferHistory[] transfers = transferService.getTransfersByUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
        System.out.println("-------------------------------");
        System.out.println("Transfers");
        System.out.println("ID     From/To          Amount");
        System.out.println("-------------------------------");

        for (TransferHistory transfer : transfers) {
            printTransferDetails(currentUser, transfer);
        }

        int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to view details (0 to cancel)");
        TransferHistory transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if (transferChoice != null) {
            Transfer transfer = transferService.getTransferByTransferId(currentUser, transferChoice.getTransferId());
            System.out.println("-------------------------------");
            System.out.println("Transfer Details");
            System.out.println("-------------------------------");
            System.out.println("Id: " + transferChoice.getTransferId());
            System.out.println("From: " + transferChoice.getFromUsername());
            System.out.println(("To: " + transferChoice.getToUsername()));
            System.out.println("Type: " + (transfer.getTransferTypeId() == 1 ? "Request" : "Send"));
            System.out.println("Status: " + (transfer.getTransferStatusId() == 2 ? "Approved" : "Rejected") );
            System.out.println("Amount: $" + transfer.getAmount());
        }

        if(transferChoice == null){
            System.out.println("Sorry your transfer number does not exist.");
        }
    }

    private void viewPendingRequests() {
        System.out.println("We did not do pending requests. Sorry.");

    }

    private void sendBucks() {
        User[] users = userService.findAll(currentUser);
        printUserOptions(currentUser, users);
        System.out.println("-------------------------------");
        System.out.println("Users");
        System.out.println("ID | Name");
        System.out.println("-------------------------------");

        for(User user : users){
            System.out.println(user.getId() + " | " + user.getUsername());
        }

        int userIdChoice = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
        int userAmount = console.getUserInputInteger("Enter Amount you would like to send (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser) && userAmount != 0) {
            Transfer transfer = new Transfer();
            transfer.setAccountTo(userIdChoice);
            transfer.setTransferTypeId(TransferStatusSend);
            //1003
            transfer.setAccountFrom(Math.toIntExact(currentUser.getUser().getId()));
            //1005
            transfer.setAmount(BigDecimal.valueOf(userAmount));
            transferService.createTransfer(currentUser, transfer);
            System.out.println("Totoro Bucks transfer Successful! Please check transfer details for receipt!");
        }

    }

    private void requestBucks() {
        User[] users = userService.findAll(currentUser);
        printUserOptions(currentUser, users);
        int userIdChoice = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            //transferService.createTransfer();
        }
    }

    private void printUserOptions(AuthenticatedUser currentUser, User[] users) {
    }

    private Transfer createTransfer (int accountChoiceUserId, String amountString, String transferType, String status){

        int transferTypeId = transferTypeService.getTransferTypeById(currentUser, Integer.parseInt(transferType)).getTransferTypeId();
        int transferStatusId = transferStatusService.getTransferStatusById(currentUser, Integer.parseInt(status)).getTransferStatusId();
        int accountToId;
        int accountFromId;
        if(transferType.equals("Send")) {
            accountToId = accountService.getAccountByUserId(currentUser, (long) accountChoiceUserId).getAccountId();
            accountFromId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        } else {
            accountToId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
            accountFromId = accountService.getAccountByUserId(currentUser, (long) accountChoiceUserId).getAccountId();
        }

        BigDecimal amount = new BigDecimal(amountString);

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountFromId);
        transfer.setAccountTo(accountToId);
        transfer.setAmount(amount);
        transfer.setTransferStatusId(transferStatusId);
        transfer.setTransferTypeId(transferTypeId);
       // transfer.setTransferId(transferId);

        //transferService.createTransfer(currentUser, transfer);
        return transfer;
    }

    private void printTransferDetails(AuthenticatedUser currentUser, TransferHistory transferChoice) {
        int id = transferChoice.getTransferId();
        String fromToDescriptor = "To:";

        //if(currentUser.getUser().getId() == transferChoice.getAccountFrom())
        if(transferChoice.getUserFromId() == currentUser.getUser().getId()){
            System.out.println(transferChoice.getTransferId() + "    To: " + transferChoice.getToUsername() + "     $" + transferChoice.getAmount() + " Totoro Bucks!");
        } else {
            System.out.println(transferChoice.getTransferId() + "    From: " + transferChoice.getFromUsername() + "     $" + transferChoice.getAmount() + " Totoro Bucks!");
        }

    }

    private TransferHistory validateTransferIdChoice(int transferIdChoice, TransferHistory[] transfers, AuthenticatedUser currentUser) {
        TransferHistory transferChoice = null;
        if (transferIdChoice != 0 && transfers.length > 0) {
            try {
                boolean validTransferIdChoice = false;
                for (TransferHistory transfer : transfers) {
                    if (transfer.getTransferId() == transferIdChoice) {
                        validTransferIdChoice = true;
                        transferChoice = transfer;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transferChoice;
    }

    private void approveOrReject(Transfer pendingTransfer, AuthenticatedUser authenticatedUser) {

        console.printApproveOrRejectOptions();
        int choice = console.getUserInputInteger("Please choose an option");

        if (choice != 0) {
            if (choice == 1) {
                int transferStatusId = transferStatusService.getTransferStatusByDesc(currentUser, "Approved").getTransferStatusId();
                pendingTransfer.setTransferStatusId(transferStatusId);
            } else if (choice == 2) {
                int transferStatusId = transferStatusService.getTransferStatusByDesc(currentUser, "Rejected").getTransferStatusId();
                pendingTransfer.setTransferStatusId(transferStatusId);
            } else {
                System.out.println("Invalid choice.");
            }
            transferService.updateTransfer(currentUser, pendingTransfer);
        }
    }


    private void exitProgram() {
        System.exit(0);
    }
}