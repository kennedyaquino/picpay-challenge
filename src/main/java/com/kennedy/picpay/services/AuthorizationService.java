package com.kennedy.picpay.services;

import com.kennedy.picpay.client.AuthorizationClient;
import com.kennedy.picpay.entities.Transfer;
import com.kennedy.picpay.exception.PicpayException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isAuthorized(Transfer transfer) {
        var resp = authorizationClient.isAuthorized();

        if(resp.getStatusCode().isError()) {
            throw  new PicpayException();
        }

        return resp.getBody().authorized();
    }
}
