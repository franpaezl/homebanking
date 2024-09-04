package com.minhub.homebanking.utils;

import com.minhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String makeAccountNumber() {
        String leftZero;

        do {
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
        } while (accountRepository.existsByAccountNumber("VIN" + leftZero));

        return "VIN" + leftZero;
    }
}
