package com.minhub.homebanking.controllers;


import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")

public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/hello")
    public String getClients(){
        return "Hello Clients";

    }

    @GetMapping("/")
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    private ResponseEntity<Client> getClientByID(@PathVariable Long id){
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }



}
