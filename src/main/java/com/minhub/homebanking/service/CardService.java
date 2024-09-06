package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardType;
import com.minhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CardService {
    Set<Card> getCardsByType(Client client, CardType cardType);
    Card createCard(CreateCardDTO createCardDTO);
    void addCard(Client client, Card card);
    void saveCard(Card card);
    void saveClient(Client client);
    void createCardForAuthenticatedClient(Authentication authentication, CreateCardDTO createCardDTO);
    Set<CardDTO> getClientCardsDTO(Client client);
}
