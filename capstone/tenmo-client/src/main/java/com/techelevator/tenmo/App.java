package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.util.List;
import java.util.Objects;

public class App {

    //Base API urls are listed below for each of the services.
    private static final String API_BASE_URL = "http://localhost:8080/";
    private static final String API_ACCOUNT_BASE_URL = API_BASE_URL + "account";
    private static final String API_USER_BASE_URL = API_BASE_URL + "user";
    private static final String API_TRANSFER_BASE_URL = API_BASE_URL + "transfer";

    //variables
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService = new AccountService(API_ACCOUNT_BASE_URL);
    private UserService userService = new UserService(API_USER_BASE_URL);
    private TransferService transferService = new TransferService(API_TRANSFER_BASE_URL);
    private AuthenticatedUser currentUser;


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

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 7) {
                handleRegisterAsAdmin();
            }else if (menuSelection == 2) {
                handleLogin();
            }  else if (menuSelection != 0) {
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

    private void handleRegisterAsAdmin() {
        String companyPassword = consoleService.promptForString("You know what to do: ");
        if (Objects.equals(companyPassword, "OneDoesNotSimplyWalkIntoThisApp")) {
            System.out.println("Please register your account.");
            UserCredentials credentials = consoleService.promptForCredentials();
            if (authenticationService.registerAsAdmin(credentials)) {
                System.out.println("Registration successful. You can now login.");
            } else {
                consoleService.printErrorMessage();
            }
        } else {
            System.out.println("Or maybe you don't...");
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        accountService.setUser(currentUser);
        transferService.setUser(currentUser);
        userService.setUser(currentUser);
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
            } else if (menuSelection == 6) {
                viewTransferByTransferId();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		accountService.getBalance(currentUser);
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.findAllTransfersForCurrentUser(currentUser);
        transferService.printTransfers(transfers);
	}

	private void viewPendingRequests() {
        Transfer[] pendingTransfersList = transferService.getPendingRequests();
        transferService.printTransfers(pendingTransfersList);
        if (pendingTransfersList.length != 0) {
            int userSelection = consoleService.promptForInt("Enter the transfer ID for the transfer you would like to approve or reject (or 0 to cancel): ");
            transferService.updatePendingTransferStatus(pendingTransfersList, userSelection);
        }
	}

	private void sendBucks() {
        userService.listAllUsers();
        transferService.sendBucks();
	}

	private void requestBucks() {
        userService.listAllUsers();
        transferService.requestBucks();
	}

    private void viewTransferByTransferId() {
        Transfer transfer = transferService.findTransferByTransferId();
        int userId = currentUser.getUser().getId();
        Account userAccount = accountService.findAccountByUserId(userId);
        if (transfer != null) {
                if (transfer.getAccountTo() == userAccount.getAccountId() || transfer.getAccountFrom() == userAccount.getAccountId()) {
                    System.out.println(transfer.transferDetailsPrintOut());
                } else {
                    System.out.println("Either no transfer was found, or you are not authorized to view the matching transfer.");
                }
        } else {
            System.out.println("...or maybe you are not authorized to view said transfer.");
        }
    }

}
