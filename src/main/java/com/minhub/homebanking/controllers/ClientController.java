package com.minhub.homebanking.controllers;

import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.dtos.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/hello")
    private String hola(){
        return  "Hola";
    }


    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> obtainClients(){
        List<Client> clients = clientRepository.findAll();
        List<ClientDTO> clientDTOs = clients.stream().map(ClientDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(clientDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainClientById(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);

        if (client == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }
}