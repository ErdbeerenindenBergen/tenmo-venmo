package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public TransferService(String url, AuthenticatedUser user) {
        this.user = user;
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

    public void printTransactions(Transfer[] transfers){
        for (Transfer transfer : transfers) {
            transfer.transferDetailsPrintOut();
        }
    }

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
