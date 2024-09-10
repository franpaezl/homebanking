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
import com.minhub.homebanking.utils.GetAuthenticatedClient;
import com.minhub.homebanking.utils.LoanValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    LoanValidate loanValidate;

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
    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanDTO> allLoans = loans.stream().map(LoanDTO::new).collect(Collectors.toList());
        return allLoans;

    }

    @Override
    public void verificatedLoan(LoanAplicationDTO loanAplicationDTO, Client client, Account account, Loan loan) {
        if (loanAplicationDTO.account().isBlank()) {
            throw new IllegalArgumentException("The transaction account field must not be empty");
        }


        if (account == null) {
            throw new IllegalArgumentException("The specified account does not exist.");
        }

        if (!client.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList()).contains(loanAplicationDTO.account())) {
            throw new IllegalArgumentException("The specified account does not belong to the authenticated client.");
        }


        if (loan == null) {
            throw new IllegalArgumentException("Loan type not found.");

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
        return loanAplicationDTO.amount() + (1 * addPercent(loanAplicationDTO));
    }


    @Override
    public ClientLoan createClientLoan(LoanAplicationDTO loanAplicationDTO) {
        return new ClientLoan(getAmountWithPercent(loanAplicationDTO), loanAplicationDTO.payments());
    }

    @Override
    public Loan processLoanApplication(LoanAplicationDTO loanAplicationDTO, Authentication authentication) {
        Client client = clientService.getAuthenticatedClient(authentication);
        Account account = accountService.findAccountByNumber(loanAplicationDTO.account());
        Loan loan = loanRepository.findById(loanAplicationDTO.id()).orElse(null);


        verificatedLoan(loanAplicationDTO, client, account, loan);


        ClientLoan clientLoan = createClientLoan(loanAplicationDTO);
        client.addClientLoans(clientLoan);
        loan.addClientLoans(clientLoan);


        Transaction transaction = transactionService.createTransactionToLoan(loanAplicationDTO);
        account.addTransaction(transaction);
        account.setBalance(account.getBalance() + loanAplicationDTO.amount());


        accountRepository.save(account);
        clientRepository.save(client);
        loanRepository.save(loan);

        return loan;
    }

}
