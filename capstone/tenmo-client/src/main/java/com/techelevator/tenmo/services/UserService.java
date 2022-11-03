package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.web.client.RestTemplate;

public class UserService {

//    public static String AUTH_TOKEN;
//    public static String USERNAME = "";
//    public static int ID;
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public UserService(String url, AuthenticatedUser user) {
        this.user = user;
        BASE_URL = url;
    }

    public void setUser(AuthenticatedUser currentUser) {
        this.user = user;
    }
}
