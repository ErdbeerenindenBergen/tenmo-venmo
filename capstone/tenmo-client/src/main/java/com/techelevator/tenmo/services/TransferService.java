package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;
    public ConsoleService consoleService;
    public UserService userService;
    public AccountService accountService;


    public TransferService(String url) {
        BASE_URL = url;
    }

    public Transfer[] findAllTransfersForCurrentUser(AuthenticatedUser user) {
        Transfer[] transferHistory = null;
        try {
            transferHistory = restTemplate.exchange(BASE_URL + "/user/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfers cannot be displayed.");
        }
        return transferHistory;
    }

    public Transfer findTransferByTransferId(int transferId) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(BASE_URL + "/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Your transfer could not be found.");
        }
        return transfer;
    }

    public String createTransfer(Transfer transfer) {
        String transferSuccessReport = "";
        try {
            transferSuccessReport = restTemplate.exchange(BASE_URL + "", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Transfer failed. Try again.");
        } return transferSuccessReport;
    }

    public void printTransactions(Transfer[] transfers){
        for (Transfer transfer : transfers) {
            transfer.transferDetailsPrintOut();
        }
    }

    public Transfer[] getPendingRequests(AuthenticatedUser user) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(BASE_URL + "/pending/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            e.printStackTrace();
        }
        return transfers;
    }

    public boolean updateTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        boolean isSuccessful = false;
        try {
            restTemplate.put(BASE_URL + "/update" + transfer.getTransferId(), entity);
            isSuccessful = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Your transfer could not be updated.");
        }
        return isSuccessful;
    }

    public void sendBucks() {
        User[] users = null;
        Transfer transfer = new Transfer();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange( "http://localhost:8080/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            for (User u : users) {
                if (u.getId() != user.getUser().getId()) {
                    System.out.println(u.getId() + "\t\t" + u.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter ID of user you are sending to (0 to cancel): ");
            transfer.setAccountTo(Integer.parseInt(scanner.nextLine()));
            transfer.setAccountFrom(user.getUser().getId());
            if (transfer.getAccountTo() != 0) {
                System.out.print("Enter the amount you would like to send: ");
                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (NumberFormatException e) {
                    System.out.println("An error occurred as you were entering the amount to transfer.");
                }
                String output = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println(output);
            }
        } catch (Exception e) {
            System.out.println("We're sorry. The input was bad.");
        }
    }


////    BELOW
//    public void sendBucks(AuthenticatedUser user) {
//        userService.findAllUsers();
//        int toUserId;
//        boolean isUserIdValid = false;
//
//        while (true) {
//            String userIdPrompt = "Enter the ID of the user you would like to send money to or enter '0' to cancel this transaction.";
//            toUserId = Integer.valueOf(consoleService.promptForInt(userIdPrompt));
//
//            if (toUserId == 0) {
//                return;
//            } else if (toUserId == user.getUser().getId()){
//                System.out.println("You can't send money to yourself!");
//                continue;
//            }
//
//            User[] allUsers = userService.findAllUsers();
//            for (User u : allUsers) {
//                if (u.getId() == (toUserId)) {
//                    isUserIdValid = true;
//                    break;
//                }
//            }
//
//            if (isUserIdValid) {
//                break;
//            } else {
//                System.out.println("You have entered an invalid user ID.");
//            }
//        }
//
//        Account fromAccount;
//        int fromAccountId;
//        Account toAccount;
//        int toAccountId;
//
//        try {
//            fromAccount = accountService.findAccountByUserId(user.getUser().getId());
//            fromAccountId = fromAccount.getAccountId();
//
//            toAccount = accountService.findAccountByUserId(user.getUser().getId());
//            toAccountId = toAccount.getAccountId();
//        } catch (Exception e) {
//            System.out.println("Unable to retrieve accounts for the transfer. Please try again.");
//            return;
//        }
//
//        String stringAskingUserForAmount = "Enter the amount you would like to transfer: ";
//        BigDecimal amount = consoleService.promptForBigDecimal(stringAskingUserForAmount);
//
//        Transfer transfer = new Transfer();
//        transfer.setAccountFrom(fromAccountId);
//        transfer.setAccountTo(toAccountId);
//        transfer.setAmount(amount);
//        transfer.setTransferTypeId(2); // send the transfer
//        transfer.setTransferStatusId(2); // approve the transfer
//
//        String transferSuccessReport = createTransfer(transfer);
//
//            System.out.println(transferSuccessReport);
//            accountService.getBalance(user);
//        }

        //ABOVE

    //SECURITY METHODS BELOW - do not alter

    public HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    public HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    //__________________________________________________________________________________________________________________


    //unused methods

    //    public boolean createTransfer(Transfer transfer) {
//        boolean wasCreated = false;
//        try {
//            ResponseEntity<Boolean> response = restTemplate.exchange(BASE_URL + "transfers/",
//                    HttpMethod.POST,
//                    makeTransferEntity(transfer),
//                    Boolean.class);
//            wasCreated = response.getBody();
//            return wasCreated;
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            System.out.println("Transfer failed. Try again.");
//        }
//        return false;
//    }

//    public Transfer[] listAllTransfers() {
//        Transfer[] allTransfers = null;
//        try {
//            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "transfers/",
//                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
//            allTransfers = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            System.out.println("Failed to retrieve transfers.");
//        }
//        return allTransfers;
//    }

//    public Transfer[] listAllTransfers() {
//        Transfer[] transfers = null;
//        try {
//            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "s/", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
//            transfers = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            System.out.println("Failed to retrieve transfers.");
//        }
//        return transfers;
//    }

}
