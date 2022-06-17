package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestUserService implements UserService{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080";

    @Override
    public User[] findAll(AuthenticatedUser authenticatedUser) {
        User[] users = null;
        try{
            users = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeEntity(authenticatedUser), User[].class).getBody();
        }catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return users;
    }

    @Override
    public User findByUsername(AuthenticatedUser authenticatedUser, String username) {
        User user = null;
        try{
            user = restTemplate.exchange(BASE_URL + "/users/{username}" + username, HttpMethod.GET,
                    makeEntity(authenticatedUser), User.class).getBody();
        }catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return user;
    }

    @Override
    public User findIdByUsername(AuthenticatedUser authenticatedUser, String username) {
        User user = null;
        try {
            user = restTemplate.exchange(BASE_URL + "/user/{username}" + username, HttpMethod.GET,
                    makeEntity(authenticatedUser), User.class).getBody();
        } catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }

        return user;
    }



    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
}

