package com.minhub.homebanking.utils;

import com.minhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CVVGenerated {

    private final CardRepository cardRepository;

    @Autowired
    public CVVGenerated(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public String generateCVV() {
        String cvvNumber;
        do {
            cvvNumber = String.format("%03d", (int) (Math.random() * 1000));
        } while (cardRepository.existsByCvv(cvvNumber)); // Verificar si el CVV ya existe

        return cvvNumber;
    }
}
