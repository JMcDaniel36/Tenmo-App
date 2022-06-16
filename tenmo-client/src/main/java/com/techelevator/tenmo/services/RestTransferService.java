package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestTransferService implements  TransferService{
    private final String baseUrl = "http://localhost:8080";;
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        try {
            restTemplate.exchange(baseUrl + "transfers/" + transfer.getTransferId(), HttpMethod.POST, entity, Transfer.class);
        } catch(RestClientResponseException e) {
                System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
    }

    @Override
    public Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, int userId) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Transfer[] transfers = null;
        try{
            transfers = restTemplate.exchange(baseUrl + "/transfers/user/" + userId, HttpMethod.GET,
                    entity,Transfer[].class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, int transferId) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(baseUrl + "/transfer/" + transferId, HttpMethod.GET,
                    entity, Transfer.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
        return transfer;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Transfer[] transfers = null;
        try{
            transfers = restTemplate.exchange(baseUrl + "/transfers", HttpMethod.GET, entity, Transfer[].class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
        return transfers;
    }

    @Override
    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        try {
            restTemplate.exchange(baseUrl + "/transfers/" + transfer.getTransferId(), HttpMethod.POST, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }
    }

    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
}
