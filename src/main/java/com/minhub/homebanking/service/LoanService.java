package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    List<LoanDTO> getAllLoansDTO();

    void verificatedLoan(LoanAplicationDTO loanAplicationDTO, Client client, Account account, Loan loan);

    double addPercent(LoanAplicationDTO loanAplicationDTO);

    double getAmountWithPercent(LoanAplicationDTO loanAplicationDTO);

    ClientLoan createClientLoan(LoanAplicationDTO loanAplicationDTO);

    Loan processLoanApplication(LoanAplicationDTO loanAplicationDTO, Authentication authentication);
}
