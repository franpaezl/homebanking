package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardColor;
import com.minhub.homebanking.models.CardType;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.CardService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.ColorType;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardsServiceImpl implements CardService {

    @Autowired
    private CardNumberGenerated cardNumberGenerated;

    @Autowired
    private CVVGenerated cvvGenerated;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;



    @Autowired
    private GetAuthenticatedClient getAuthenticatedClient;

    @Autowired
    private ClientService clientService;


    @Override
    public String generateCVV() {
        return cvvGenerated.generateFourDigitNumber();
    }

    @Override
    public Card createCard(CreateCardDTO createCardDTO) {
        String newCardType = createCardDTO.cardType().toUpperCase();
        String newCardColor = createCardDTO.cardColor().toUpperCase();

        CardColor cardColor = CardColor.valueOf(newCardColor);
        CardType cardType = CardType.valueOf(newCardType);

        return new Card(
                cardType,
                cardColor,
                cardNumberGenerated.generateCardNumber(),
                generateCVV(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(5)
        );
    }


    @Override
    public void validateCard(Set<Card> existingCards, CreateCardDTO createCardDTO) {
        String newCardType = createCardDTO.cardType();//DEBIT

        String newCardColor = createCardDTO.cardColor();//GOLD

        if (newCardType == null || newCardType.isBlank()) {
            throw new IllegalArgumentException("Card type cannot be empty.");
        }

        if (newCardColor == null || newCardColor.isBlank()) {
            throw new IllegalArgumentException("Card color cannot be empty.");
        }

        CardType enumCardType;
        CardColor enumColorType;

        try {
            enumCardType = CardType.valueOf(newCardType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card type: " + newCardType);
        }

        try {
            enumColorType = CardColor.valueOf(newCardColor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card color: " + newCardColor);
        }


        long countByType = existingCards.stream()
                .filter(card -> card.getType() == enumCardType)
                .count();

        if (countByType >= 3) {
            throw new IllegalArgumentException("You have reached the maximum number of allowed " + newCardType + " cards (3)");
        }


        boolean colorExists = existingCards.stream()
                .anyMatch(card -> card.getType() == enumCardType && card.getColor() == enumColorType);

        if (colorExists) {
            throw new IllegalArgumentException("A card with color " + newCardColor + " already exists for this card type.");
        }
    }


    @Override
    public Card createCardForAuthenticatedClient(Authentication authentication, CreateCardDTO createCardDTO) {
        Client client = clientService.getAuthenticatedClient(authentication);
        Set<Card> existingCards = client.getCards();

        validateCard(existingCards, createCardDTO);

        Card newCard = createCard(createCardDTO);
        clientService.addCardsToClient(client, newCard);

        cardRepository.save(newCard);
        clientService.saveClient(client);

        return newCard;
    }

    @Override
    public Set<CardDTO> getClientCardsDTO(Client client) {
        return client.getCards().stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<CardDTO> getAuthenticatedClientCardsDTO(Authentication authentication) {
        Client client = clientService.getAuthenticatedClient(authentication);
        return getClientCardsDTO(client);
    }
}
