package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
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

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public Account findAccountByUserId(@PathVariable int userId) {
        return accountDao.findAccountByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account findAccountById(@PathVariable int accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }

    //FOR BELOW: NEED accountDao.create/update/delete method to create account
    //Not sure if we need these yet

//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "/", method = RequestMethod.POST)
//    public boolean create(@Valid @RequestBody Account account) {
//        return accountDao.create(account);
//    }

//    @RequestMapping(path = "/{accountId}", method = RequestMethod.PUT)
//    public boolean update(@Valid @PathVariable int accountId, @RequestBody Account account) {
//        return accountDao.update(accountId, account);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @RequestMapping(path = "/{accountId}", method = RequestMethod.DELETE)
//    public boolean delete(@Valid @PathVariable int id) {
//        return accountDao.delete(accountId);
//    }

}