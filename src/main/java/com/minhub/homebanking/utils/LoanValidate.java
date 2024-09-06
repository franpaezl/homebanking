package com.minhub.homebanking.utils;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Loan;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LoanValidate {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LoanRepository loanRepository;

    public void isAccountFieldEmpty(LoanAplicationDTO loanAplicationDTO, Client client,Account account,Loan loan) {
        if (loanAplicationDTO.account().isBlank()) {
            throw new IllegalArgumentException("The transaction account field must not be empty");
        }


        if (account == null) {
            throw new IllegalArgumentException("The specified account does not exist.");
        }

        if (!client.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList()).contains(loanAplicationDTO.account())) {
            throw new IllegalArgumentException("The specified account does not belong to the authenticated client.");
        }


        if (loan == null){
            throw new IllegalArgumentException("Loan type not found.");

        }
        if (loanAplicationDTO.amount() > loan.getMaxAmount()) {
            throw new IllegalArgumentException("The loan amount exceeds the maximum amount allowed.");
        }

        if (!loan.getPayment().contains(loanAplicationDTO.payments())) {
            throw new IllegalArgumentException("The number of payments is not valid for the selected loan.");

        }


        }
}
