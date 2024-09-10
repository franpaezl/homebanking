package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.CardService;
import com.minhub.homebanking.utils.CVVGenerated;
import com.minhub.homebanking.utils.CardNumberGenerated;
import com.minhub.homebanking.utils.CardValidate;
import com.minhub.homebanking.utils.GetAuthenticatedClient;
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

    @Autowired
    private GetAuthenticatedClient getAuthenticatedClient;



    @Override
    public String generateCVV(){
        String cvv;
        do {
            cvv = cvvGenerated.generateFourDigitNumber();
        }
        while (cardRepository.existsByCvv(cvv));

        return cvv;
    }

    @Override
    public Card createCard(CreateCardDTO createCardDTO) {
        return new Card(
                createCardDTO.cardType(),
                createCardDTO.cardColor(),
                cardNumberGenerated.generateCardNumber(),
                generateCVV(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(5)
        );
    }



    @Override
    public Card createCardForAuthenticatedClient(Authentication authentication, CreateCardDTO createCardDTO) {
        Client client = getAuthenticatedClient.getAuthenticatedClient(authentication);
        Set<Card> existingCards = client.getCards();

        cardValidate.validateCards(existingCards, createCardDTO);

        Card newCard = createCard(createCardDTO);
        client.addCards(newCard);

        cardRepository.save(newCard);
        clientRepository.save(client);

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
        Client client = getAuthenticatedClient.getAuthenticatedClient(authentication);
        return getClientCardsDTO(client);
    }
}
