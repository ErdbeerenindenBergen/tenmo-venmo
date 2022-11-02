package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    public void addToBalance(int userId, BigDecimal amount) {
        Account account = new Account();
        int accountId = restTemplate.exchange(BASE_URL + "account/user/" + userId, HttpMethod.GET, makeAuthEntity(), int.class).getBody();
        account.setaccountId(accountId);
        BigDecimal balance;
        balance = restTemplate.exchange(BASE_URL + "account/balance" + userId, HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        account.setBalance(balance.subtract(amount));
        account.setUserId(userId);
        restTemplate.exchange(BASE_URL + "account/" + userId, HttpMethod.PUT, makeAccountEntity(account), Account.class);
    }


    public HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(UserService.AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    public HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(UserService.AUTH_TOKEN);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        return entity;
    }
}


