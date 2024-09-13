

import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardColor;
import com.minhub.homebanking.models.CardType;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.CardService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.service.impl.CardsServiceImpl;
import com.minhub.homebanking.utils.CVVGenerated;
import com.minhub.homebanking.utils.CardNumberGenerated;
import com.minhub.homebanking.utils.GetAuthenticatedClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class CardTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardService cardService;

    private Client testClient;
    private CardNumberGenerated cardNumberGenerated;
    private CVVGenerated cvvGenerated;
    private ClientService clientService;
    private GetAuthenticatedClient getAuthenticatedClient;

    @BeforeEach
    public void setUp() {
        cardNumberGenerated = new CardNumberGenerated() {
            @Override
            public String generateCardNumber() {
                return "1234567812345678";
            }
        };

        cvvGenerated = new CVVGenerated() {
            @Override
            public String generateFourDigitNumber() {
                return "123";
            }
        };

        clientService = new ClientService() {
            @Override
            public Client getAuthenticatedClient(Authentication authentication) {
                return testClient;
            }

            @Override
            public void addCardsToClient(Client client, Card card) {
                client.getCards().add(card);
            }

            @Override
            public void saveClient(Client client) {
                clientRepository.save(client);
            }
        };

        getAuthenticatedClient = new GetAuthenticatedClient() {
            @Override
            public Client getClient(Authentication authentication) {
                return testClient;
            }
        };



        testClient = new Client("John", "Doe", "john.doe@gmail.com", "password");
        clientRepository.save(testClient);
    }

    @Test
    public void createCard_ShouldCreateCardSuccessfully() {
        CreateCardDTO createCardDTO = new CreateCardDTO("DEBIT", "GOLD");
        Card newCard = cardsService.createCard(createCardDTO);

        assertThat(newCard, notNullValue());
        assertThat(newCard.getType(), is(CardType.DEBIT));
        assertThat(newCard.getColor(), is(CardColor.GOLD));
        assertThat(newCard.getNumber(), is("1234567812345678"));
        assertThat(newCard.getCvv(), is("123"));
        assertThat(newCard.getThruDate(), is(LocalDateTime.now().plusYears(5)));
    }

    @Test
    public void validateCard_ShouldThrowException_WhenTypeIsEmpty() {
        CreateCardDTO createCardDTO = new CreateCardDTO("", "GOLD");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cardsService.validateCard(new HashSet<>(), createCardDTO);
        });

        assertThat(exception.getMessage(), is("Card type cannot be empty."));
    }

    @Test
    public void validateCard_ShouldThrowException_WhenColorIsEmpty() {
        CreateCardDTO createCardDTO = new CreateCardDTO("DEBIT", "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cardsService.validateCard(new HashSet<>(), createCardDTO);
        });

        assertThat(exception.getMessage(), is("Card color cannot be empty."));
    }

    @Test
    public void validateCard_ShouldThrowException_WhenTypeIsInvalid() {
        CreateCardDTO createCardDTO = new CreateCardDTO("INVALID", "GOLD");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cardsService.validateCard(new HashSet<>(), createCardDTO);
        });

        assertThat(exception.getMessage(), is("Invalid card type: INVALID"));
    }

    @Test
    public void validateCard_ShouldThrowException_WhenColorIsInvalid() {
        CreateCardDTO createCardDTO = new CreateCardDTO("DEBIT", "INVALID");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cardsService.validateCard(new HashSet<>(), createCardDTO);
        });

        assertThat(exception.getMessage(), is("Invalid card color: INVALID"));
    }

    @Test
    public void createCardForAuthenticatedClient_ShouldCreateAndSaveCard() {
        CreateCardDTO createCardDTO = new CreateCardDTO("DEBIT", "GOLD");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card newCard = cardsService.createCardForAuthenticatedClient(authentication, createCardDTO);

        assertThat(newCard, notNullValue());
        assertThat(newCard.getType(), is(CardType.DEBIT));
        assertThat(newCard.getColor(), is(CardColor.GOLD));
        assertThat(testClient.getCards(), hasItem(newCard));
        assertThat(cardRepository.findById(newCard.getId()), is(notNullValue()));
    }

    @Test
    public void getClientCardsDTO_ShouldReturnCardDTOs() {
        Card card = new Card(
                CardType.CREDIT,
                CardColor.SILVER,
                "1234567812345678",
                "123",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(5)
        );

        testClient.getCards().add(card);
        clientRepository.save(testClient);

        Set<CardDTO> cardDTOs = cardsService.getClientCardsDTO(testClient);

        assertThat(cardDTOs, hasSize(1));
        assertThat(cardDTOs.iterator().next().getType(), is(CardType.CREDIT));
        assertThat(cardDTOs.iterator().next().getColor(), is(CardColor.SILVER));
    }
}
