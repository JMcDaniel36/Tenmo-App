package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestAccountServices implements AccountService{
    private final String baseUrl = "http://localhost:8080";;
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public Balance getBalance(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Balance balance = null;
        try{
            balance = restTemplate.exchange(baseUrl + "/balance", HttpMethod.GET, entity, Balance.class).getBody();
        } catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return balance;
    }

    @Override
    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, Long userId) {
        Account account = null;
        HttpEntity entity = createHttpEntity(authenticatedUser);
        try {
            account = restTemplate.exchange(baseUrl + "account/user/" + userId,
                    HttpMethod.GET, entity, Account.class).getBody();
        }  catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return account;
    }

    @Override
    public Account getAccountById(AuthenticatedUser authenticatedUser, int accountId) {
        Account account = null;
        HttpEntity entity = createHttpEntity(authenticatedUser);
        try{
            account = restTemplate.exchange(baseUrl + "account/" + accountId,
                    HttpMethod.GET, entity, Account.class).getBody();
        } catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }

        return account;
    }

    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
}
