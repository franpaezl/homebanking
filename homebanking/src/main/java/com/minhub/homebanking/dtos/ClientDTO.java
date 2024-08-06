package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;

    public ClientDTO(){

    }

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

}