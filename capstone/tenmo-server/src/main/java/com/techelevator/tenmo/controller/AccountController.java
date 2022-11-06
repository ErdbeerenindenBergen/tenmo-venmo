package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
//"transfer" is added to BASE_API_URL
@RequestMapping("account")
public class AccountController {

    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/user/{userId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int userId) {
        return accountDao.getBalance(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public Account findAccountByUserId(@PathVariable int userId) {
        return accountDao.findAccountByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account findAccountById(@PathVariable int accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }


}