package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestTransferTypeService implements TransferTypeService{
    private final String baseUrl = "http://localhost:8080";;
    private final RestTemplate restTemplate = new RestTemplate();

    public RestTransferTypeService(String apiBaseUrl) {
    }


    @Override
    public TransferType getTransferTypeByDesc(AuthenticatedUser authenticatedUser, String description) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        TransferType transferType = null;
        try{
            transferType = restTemplate.exchange(baseUrl + "/transfertype/filter?description=" + description, HttpMethod.GET,
                    entity, TransferType.class).getBody();
        } catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return transferType;
    }

    @Override
    public TransferType getTransferTypeById(AuthenticatedUser authenticatedUser, int transferId) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        TransferType transferType = null;
        try{
            transferType = restTemplate.exchange(baseUrl + "transfertype/" + transferId, HttpMethod.GET,
                    entity, TransferType.class).getBody();
        } catch(RestClientResponseException e){
            System.out.println("Unable to complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Unable to complete request due to server network issue. Please try again.");
        }
        return transferType;
    }
    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
}
