package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    //make sure "user" is added to BASE_API_URL

    //UserDao
    //completed
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "s", method = RequestMethod.GET)
    public List<User> listAllUsers() {
        return userDao.findAll();
    }

    //the following methods may be overkill with the "if null" statements if they have already been coded in the JdbcUserDao with exceptions
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/{userId}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable int userId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return user;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/{username}", method = RequestMethod.GET)
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return user;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/userId/{username}", method = RequestMethod.GET)
    public int findIdByUsername(String username) {
        return userDao.findIdByUsername(username);
    }

        //simplified methods without "if statements" below

//    @RequestMapping(path = "s", method = RequestMethod.GET)
//    public List<User> findAll() {
//        return userDao.findAll();
//    }
//
//    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
//    public User getUserById(@PathVariable int id) {
//        return userDao.getUserById(id);
//    }
//
//    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
//    public User findByUsername(String username) {
//        return userDao.findByUsername(username);
//    }
//
//    @RequestMapping(path = "/userId/{username}", method = RequestMethod.GET)
//    public int findIdByUsername(String username) {
//        return userDao.findIdByUsername(username);
//    }


}
