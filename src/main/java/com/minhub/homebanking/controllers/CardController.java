package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private CardService cardService;

    @PostMapping("/current/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO, Authentication authentication) {
        try {
            cardService.createCardForAuthenticatedClient(authentication, createCardDTO);
            return new ResponseEntity<>("Card created", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<CardDTO> cardDto = cardService.getClientCardsDTO(client);

        return new ResponseEntity<>(cardDto, HttpStatus.OK);
    }
}
