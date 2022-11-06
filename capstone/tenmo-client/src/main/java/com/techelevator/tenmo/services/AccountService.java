package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public AccountService(String url) {
        BASE_URL = url;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }


    public BigDecimal getBalance(AuthenticatedUser user) {
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(BASE_URL + "/user/" + user.getUser().getId() + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
            System.out.println("Your current account balance is: $" + balance);
        } catch (RestClientException e) {
            System.out.println("Your balance could not be retrieved.");
        }
        return balance;
    }

    public Account findAccountByUserId(int userId) {
        Account account = null;
        try {
            account = restTemplate.exchange(BASE_URL + "/user/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
        } catch (RestClientException e) {
            System.out.println("This account could not be found.");
        }
        return account;
    }

    public HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    public HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(account, headers);
    }
}

