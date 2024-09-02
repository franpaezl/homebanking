package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.CardColor;
import com.minhub.homebanking.models.CardType;

public record CreateCardDTO(CardType cardType, CardColor cardColor) {
}
