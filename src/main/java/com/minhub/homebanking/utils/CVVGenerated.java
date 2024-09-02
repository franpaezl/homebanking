package com.minhub.homebanking.utils;

import com.minhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CVVGenerated {

    @Autowired
    private static CardRepository cardRepository;

    public CVVGenerated(CardRepository cardRepository) {
        CVVGenerated.cardRepository = cardRepository;
    }

    public static String generateCVV() {
        String cvvNumber;

        do {
            cvvNumber = String.format("%03d", (int) (Math.random() * 1000));
        } while (cardRepository.existsByCvv(cvvNumber));

        return cvvNumber;
    }
}
