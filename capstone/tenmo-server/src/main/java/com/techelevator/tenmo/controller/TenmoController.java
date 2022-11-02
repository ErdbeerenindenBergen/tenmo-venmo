package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    @Autowired
    AccountDao accountDao;
    @Autowired
    UserDao userDao;
//    @Autowired
//    TransferDao transferDao;

    Account account;

    //AccountDao
@RequestMapping(path = "/account/user/{id}", method = RequestMethod.GET)
    public Account findByUserId(@PathVariable int id) {
        return accountDao.findUserById(id);
    }

@RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public Account findAccountById(@PathVariable int id) {
    return accountDao.findAccountById(id);
    }

//@RequestMapping(path = "/account/balance", method = RequestMethod.GET)
//    public BigDecimal getBalance(@PathVariable Principal principal) {
//    System.out.println(principal.getName());
//    BigDecimal balance = new BigDecimal(String.valueOf(accountDao.getBalance((principal.getName())));
//    return balance;
//}

    //UserDao
@RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUsers() {
    return userDao.findAll();
}

@RequestMapping (path = "/users/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
    return userDao.getUserById(id);
}




}
