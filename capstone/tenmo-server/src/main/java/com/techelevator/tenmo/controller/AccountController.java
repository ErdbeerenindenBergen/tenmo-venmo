package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    //methods for adding/subtracting from balance may not be necessary in the controller

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/account/user/{userId}", method = RequestMethod.GET)
    public Account findAccountByUserId(@PathVariable int userId) {
        //need to rename method below in AccountDao so that it is clear we are searching for account and not user
        return accountDao.findUserById(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
    //could rename method below in AccountDao to make it clearer to: findAccountByAccountId
    public Account findAccountById(@PathVariable int accountId) {

        return accountDao.findAccountById(accountId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/account/balance/{userId}", method = RequestMethod.GET)
    //wondering if we should use @PathVariable Principle principle
    public BigDecimal getBalance(@PathVariable int userId) {
        return accountDao.getBalance(userId);
    }
}