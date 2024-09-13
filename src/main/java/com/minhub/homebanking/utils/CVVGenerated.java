package com.minhub.homebanking.utils;

import com.minhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class CVVGenerated {

    public  String generateFourDigitNumber() {
        // Genera un número entero aleatorio entre 0 y 9999
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 10000);
        // Formatea el número para que siempre tenga 4 cifras, incluyendo ceros a la izquierda
        return String.format("%04d", randomNumber);
    }
}
