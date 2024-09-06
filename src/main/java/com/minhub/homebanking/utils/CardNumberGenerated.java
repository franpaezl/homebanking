package com.minhub.homebanking.utils;

import org.springframework.stereotype.Component;

@Component
public class CardNumberGenerated {

    public String generateCardNumber() {
        StringBuilder cardNumberBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String randomNumbers = String.format("%04d", (int) (Math.random() * 10000));
            cardNumberBuilder.append(randomNumbers);
            if (i < 3) {
                cardNumberBuilder.append("-");
            }
        }
        return cardNumberBuilder.toString();
    }
}
