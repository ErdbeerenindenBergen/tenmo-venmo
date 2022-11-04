package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public UserService(String url) {
        BASE_URL = url;
    }

    public User findUserById(int id) {
        User user = null;
        try {
            user = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("User not found.");
        }
        return user;
    }

    public User[] findAllUsers() {
        User[] allUsers = null;
        try {
            allUsers = restTemplate.exchange(BASE_URL + "s", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Users not found.");
        } return allUsers;
    }

    public void setUser(AuthenticatedUser currentUser) {
        this.user = user;
    }

    //not working for some reason
//    public HttpEntity<User> makeUserEntity(AuthenticatedUser user) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(user.getToken());
//        return new HttpEntity<>(user, headers);
//    }

    public HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

}

//_____________________________________________________________________________________________________________________

//    public void listAllUsers(AuthenticatedUser currentUser) {
//        User[] allUsers = findAllUsers();
//        System.out.println("-----------------------------------------------------");
//        System.out.println("LIST OF AVAILABLE USERS");
//        System.out.println("-----------------------------------------------------");
//        System.out.println("USERS");
//        System.out.println("ID\t\t\tNAME");
//        System.out.println("-----------------------------------------------------");
//        for (User user : allUsers) {
//            if (user.getUserId().equals(currentUser.getUser().getUserId())){
//                continue;
//            }
//            System.out.println(user.getUserId() + "\t\t" + user.getUsername());
//        }
//        System.out.println("-----------------------------------------------------");
//        System.out.println();
//    }