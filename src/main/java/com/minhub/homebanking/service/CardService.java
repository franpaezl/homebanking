package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CardService {
    String generateCVV();

    Card createCard(CreateCardDTO createCardDTO);

    void validateCard(Set<Card> existingCards, CreateCardDTO createCardDTO);

    Card createCardForAuthenticatedClient(Authentication authentication, CreateCardDTO createCardDTO);

    Set<CardDTO> getClientCardsDTO(Client client);

    Set<CardDTO> getAuthenticatedClientCardsDTO(Authentication authentication);
}
