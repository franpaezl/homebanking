package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.models.Card;
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
    private CardService cardService;

    @PostMapping("/current/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO, Authentication authentication) {
        try {
           Card card = cardService.createCardForAuthenticatedClient(authentication, createCardDTO);
            return new ResponseEntity<>("Card created", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication) {
        Set<CardDTO> cardDto = cardService.getAuthenticatedClientCardsDTO(authentication);
        return new ResponseEntity<>(cardDto, HttpStatus.OK);
    }
}
