package com.minhub.homebanking.utils;

import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class GetAuthenticatedClient {

    @Autowired
    ClientRepository clientRepository;

    public Client getAuthenticatedClient(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName());
    }
}
