package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.*;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getAllLoans();

    void verificatedLoan(LoanAplicationDTO loanAplicationDTO, Client client, Account account, Loan loan);

    double addPercent(LoanAplicationDTO loanAplicationDTO);

    double getAmountWithPercent(LoanAplicationDTO loanAplicationDTO);

    ClientLoan createClientLoan(LoanAplicationDTO loanAplicationDTO);

    Transaction createTransaction(LoanAplicationDTO loanAplicationDTO);

    Loan processLoanApplication(LoanAplicationDTO loanAplicationDTO, Client client);
}
