package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    Account account;

    //AccountDao
@RequestMapping(path = "/account/user/{userId}", method = RequestMethod.GET)
    public Account findByUserId(@PathVariable int id) {
        return accountDao.findUserById(id);
    }

@RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
    public Account findAccountById(@PathVariable int id) {
    return accountDao.findAccountById(id);
    }

//methods for adding/subtracting from balance may not be necessary

@RequestMapping(path = "/account/balance/{userId}", method = RequestMethod.GET)
//wondering if we should use @PathVariable Principle principle
    public BigDecimal getBalance(@PathVariable int id) {
    return accountDao.getBalance(id);
}

//UserDao
@RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> findAll() {
    return userDao.findAll();
}

@RequestMapping (path = "/users/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
    return userDao.getUserById(id);
}

@RequestMapping (path = "/users/{username}", method = RequestMethod.GET)
    public User findByUsername(String username) {
    return userDao.findByUsername(username);
}

@RequestMapping (path = "/userId/{username}", method = RequestMethod.GET)
    public int findIdByUsername(String username) {
    return userDao.findIdByUsername(username);
}
//UserDao methods completed



}
