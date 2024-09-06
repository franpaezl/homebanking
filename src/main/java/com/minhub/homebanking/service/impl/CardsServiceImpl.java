package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardType;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.CardService;
import com.minhub.homebanking.utils.CVVGenerated;
import com.minhub.homebanking.utils.CardNumberGenerated;
import com.minhub.homebanking.utils.CardValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    private CardValidate cardValidate;

    @Override
    public Set<Card> getCardsByType(Client client, CardType cardType) {
        return client.getCards().stream()
                .filter(card -> card.getType() == cardType)
                .collect(Collectors.toSet());
    }

    @Override
    public Card createCard(CreateCardDTO createCardDTO) {
        return new Card(createCardDTO.cardType(),
                createCardDTO.cardColor(),
                cardNumberGenerated.generateCardNumber(),
                cvvGenerated.generateCVV(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(5));
    }

    @Override
    public void addCard(Client client, Card card) {
        client.addCards(card);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void createCardForAuthenticatedClient(Authentication authentication, CreateCardDTO createCardDTO) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> existingCards = client.getCards();

        // Validar la tarjeta
        cardValidate.validateCards(existingCards, createCardDTO);

        // Crear la tarjeta
        Card newCard = createCard(createCardDTO);
        addCard(client, newCard);

        // Guardar tarjeta
        saveCard(newCard);

        // Guardar cliente
        saveClient(client);
    }

    @Override
    public Set<CardDTO> getClientCardsDTO(Client client) {
        return client.getCards().stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }
}
