package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public AccountService(String url, AuthenticatedUser user) {
        this.user = user;
        BASE_URL = url;
    }

    public BigDecimal getBalance(AuthenticatedUser user) {
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(BASE_URL + "/balance/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
            System.out.println("Your current account balance is: $" + balance);
        } catch (RestClientException e) {
            System.out.println("Your balance could not be retrieved.");
        }
        return balance;
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

//_____________________________________________________________________________________________________________________


//    public void addToBalance(int userId, BigDecimal amount) {
//        Account account = new Account();
//        int accountId = restTemplate.exchange(BASE_URL + "account/user/" + userId, HttpMethod.GET, makeAuthEntity(), int.class).getBody();
//        account.setAccountId(accountId);
//        BigDecimal balance;
//        balance = restTemplate.exchange(BASE_URL + "account/balance" + userId, HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
//        account.setBalance(balance.subtract(amount));
//        account.setUserId(userId);
//        restTemplate.exchange(BASE_URL + "account/" + userId, HttpMethod.PUT, makeAccountEntity(account), Account.class);
//    }

