package com.minhub.homebanking.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGenerator {


    public static String generateEightDigitNumber() {
        // Genera un número entero aleatorio entre 1 y 99999999
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 100000000);
        // Formatea el número para que siempre tenga 8 cifras, incluyendo ceros a la izquierda
        return String.format("%08d", randomNumber);
    }


}