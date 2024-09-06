package com.minhub.homebanking.utils;

import com.minhub.homebanking.dtos.CreateCardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardColor;
import com.minhub.homebanking.models.CardType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CardValidate {

    public void validateCards(Set<Card> existingCards, CreateCardDTO createCardDTO) {
        CardType newCardType = createCardDTO.cardType();
        CardColor newCardColor = createCardDTO.cardColor();

        // Contar tarjetas del tipo especificado
        long countByType = existingCards.stream()
                .filter(card -> card.getType() == newCardType)
                .count();

        // Validar el número máximo de tarjetas por tipo
        if (countByType >= 3) {
            throw new IllegalArgumentException("You have reached the maximum number of allowed " + newCardType + " cards (3)");
        }

        // Verificar si ya existe una tarjeta con el mismo color para el tipo especificado
        boolean colorExists = existingCards.stream()
                .anyMatch(card -> card.getType() == newCardType && card.getColor() == newCardColor);

        if (colorExists) {
            throw new IllegalArgumentException("A card with color " + newCardColor + " already exists for this card type.");
        }
    }
}
