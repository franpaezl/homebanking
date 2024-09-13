package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.LoanRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.service.LoanService;
import com.minhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;


    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    TransactionService transactionService;


    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();

    }

    @Override
    public List<LoanDTO> getAllLoansDTO() {
        return getAllLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public void verificatedLoan(LoanAplicationDTO loanAplicationDTO, Client client, Account account, Loan loan) {
        if (loanAplicationDTO.accountNumber().isBlank() || loanAplicationDTO.accountNumber() == null) {
            throw new IllegalArgumentException("The transaction account field must not be empty");
        }

        if (account == null) {
            throw new IllegalArgumentException("The specified account does not exist.");
        }

        if (!client.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList()).contains(loanAplicationDTO.accountNumber())) {
            throw new IllegalArgumentException("The specified account does not belong to the authenticated client.");
        }


        if (loan == null) {
            throw new IllegalArgumentException("Loan type not found.");

        }
        if (loanAplicationDTO.amount() < 5000) {
            throw new IllegalArgumentException("The loan amount does not meet the minimum amount required.");
        }


        if (loanAplicationDTO.amount() > loan.getMaxAmount()) {
            throw new IllegalArgumentException("The loan amount exceeds the maximum amount allowed.");
        }

        if (!loan.getPayment().contains(loanAplicationDTO.payments())) {
            throw new IllegalArgumentException("The number of payments is not valid for the selected loan.");

        }


    }


    @Override
    public double addPercent(LoanAplicationDTO loanAplicationDTO) {
        if (loanAplicationDTO.payments() < 12) {
            return loanAplicationDTO.amount() * 0.15;
        } else if (loanAplicationDTO.payments() == 12) {
            return loanAplicationDTO.amount() * 0.20;
        } else {
            return loanAplicationDTO.amount() * 0.25;
        }
    }

    @Override
    public double getAmountWithPercent(LoanAplicationDTO loanAplicationDTO) {
        return loanAplicationDTO.amount() + addPercent(loanAplicationDTO);
    }


    @Override
    public ClientLoan createClientLoan(LoanAplicationDTO loanAplicationDTO) {
        return new ClientLoan(getAmountWithPercent(loanAplicationDTO), loanAplicationDTO.payments());
    }

    @Override
    public Loan processLoanApplication(LoanAplicationDTO loanAplicationDTO, Authentication authentication) {
        Client client = clientService.getAuthenticatedClient(authentication);
        Account account = accountService.findAccountByNumber(loanAplicationDTO.accountNumber());
        Loan loan = loanRepository.findById(loanAplicationDTO.id()).orElse(null);


        verificatedLoan(loanAplicationDTO, client, account, loan);


        ClientLoan clientLoan = createClientLoan(loanAplicationDTO);
        clientService.addClientLoanToClient(client, clientLoan);
        loan.addClientLoans(clientLoan);


        Transaction transaction = transactionService.createTransactionToLoan(loanAplicationDTO);
        accountService.addTransactionToAccount(account, transaction);

        accountService.addAmountToAccount(account, loanAplicationDTO.amount());



        accountService.saveAccount(account);
        clientService.saveClient(client);
        loanRepository.save(loan);

        return loan;
    }

}
