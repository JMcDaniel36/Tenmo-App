package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestTransferStatusService implements  TransferStatusService{

    private final String baseUrl ="http://localhost:8080";;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, int transferStatusId) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        TransferStatus transferStatus = null;
        try{
            transferStatus = restTemplate.exchange(baseUrl + "/transferstatus" + transferStatusId, HttpMethod.GET,
                    entity, TransferStatus.class).getBody();
        }catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusByDesc(AuthenticatedUser authenticatedUser, String description) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        TransferStatus transferStatus = null;
        try{
            transferStatus = restTemplate.exchange(baseUrl + "/transferstatus/filter?description=" + description,
                    HttpMethod.GET, entity, TransferStatus.class).getBody();
        }catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return transferStatus;
    }
    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
}
