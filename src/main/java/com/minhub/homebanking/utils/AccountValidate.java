package com.minhub.homebanking.utils;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import org.springframework.stereotype.Component;

@Component
public class AccountValidate {
    public void maxAccountsNotExceeded(Client client) {
        if(client.getAccounts().size() == 3) {
            throw new IllegalArgumentException("You cannot create a new account at this time. You have reached the maximum number of allowed accounts (3)");
        }
    }

}
