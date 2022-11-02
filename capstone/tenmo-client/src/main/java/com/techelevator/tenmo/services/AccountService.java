package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final String BASE_URL;
//    private final String ACCOUNT_BASE_URL = BASE_URL + "/account";
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public AccountService(String url, AuthenticatedUser user) {
        this.user = user;
        BASE_URL = url;
    }

    public void addToBalance(int userId, BigDecimal amount) {

        Account account = new Account();
        int accountId = restTemplate.exchange(BASE_URL + "account"
    }
}


