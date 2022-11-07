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

    public void setUser(AuthenticatedUser user) {
        this.user = user;
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
            allUsers = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Users not found.");
        } return allUsers;
    }

    public void listAllUsers() {
        User[] allUsers = findAllUsers();
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("LIST OF AVAILABLE USERS");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("Users");
        System.out.println("ID\t\t\t\tName");
        System.out.println("-------------------------------------------------------------------------------------------");
        for (User u : allUsers) {
            if (u.getId() != user.getUser().getId()){
                System.out.println(u.getId() + "\t\t\t" + u.getUsername());
            }
        }
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println();
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