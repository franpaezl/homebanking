package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.LoanRepository;
import com.minhub.homebanking.service.LoanService;
import com.minhub.homebanking.utils.LoanValidate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanDTO> allLoans = loans.stream().map(LoanDTO::new).collect(Collectors.toList());
        return allLoans;

    }

    @Override
    public void verificatedLoan(LoanAplicationDTO loanAplicationDTO, Client client,Account account, Loan loan) {
        loanValidate.isAccountFieldEmpty(loanAplicationDTO, client, account,loan);
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
    public Transaction createTransaction(LoanAplicationDTO loanAplicationDTO) {
        return new Transaction(loanAplicationDTO.amount(), "Solicit loan", LocalDateTime.now(), TransactionType.CREDIT);
    }

    @Override
    public Loan processLoanApplication(LoanAplicationDTO loanAplicationDTO, Client client) {
        Account account = accountRepository.findByAccountNumber(loanAplicationDTO.account());
        Loan loan = loanRepository.findById(loanAplicationDTO.id()).orElseThrow(() -> new IllegalArgumentException("Loan type not found."));


        verificatedLoan(loanAplicationDTO, client, account, loan);


        ClientLoan clientLoan = createClientLoan(loanAplicationDTO);
        client.addClientLoans(clientLoan);
        loan.addClientLoans(clientLoan);


        Transaction transaction = createTransaction(loanAplicationDTO);
        account.addTransaction(transaction);
        account.setBalance(account.getBalance() + loanAplicationDTO.amount());


        accountRepository.save(account);
        clientRepository.save(client);
        loanRepository.save(loan);

        return loan;
    }

}
