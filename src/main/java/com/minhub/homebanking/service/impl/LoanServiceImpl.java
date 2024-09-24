package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientLoanRepository;
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
    ClientLoanRepository clientLoanRepository;

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
        // Validar que el número de cuenta no esté vacío o sea nulo
        if (loanAplicationDTO.accountNumber().isBlank() || loanAplicationDTO.accountNumber() == null) {
            throw new IllegalArgumentException("The transaction account field must not be empty");
        }

        // Validar que la cuenta exista
        if (account == null) {
            throw new IllegalArgumentException("The specified account does not exist.");
        }

        // Verificar que la cuenta pertenezca al cliente autenticado
        if (!client.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList()).contains(loanAplicationDTO.accountNumber())) {
            throw new IllegalArgumentException("The specified account does not belong to the authenticated client.");
        }

        // Validar que el tipo de préstamo exista
        if (loan == null) {
            throw new IllegalArgumentException("Loan type not found.");
        }

        // Validar que el cliente no tenga ya un préstamo del mismo tipo
        if (clientLoanRepository.existsByClientAndLoanId(client, loan.getId())) {
            throw new IllegalArgumentException("The client already has a loan of this type.");
        }

        // Validar que el monto del préstamo sea mayor al mínimo requerido
        if (loanAplicationDTO.amount() < 5000) {
            throw new IllegalArgumentException("The loan amount does not meet the minimum amount required.");
        }

        // Validar que el monto del préstamo no exceda el máximo permitido
        if (loanAplicationDTO.amount() > loan.getMaxAmount()) {
            throw new IllegalArgumentException("The loan amount exceeds the maximum amount allowed.");
        }

        // Validar que el número de pagos sea válido para el tipo de préstamo seleccionado
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

        // Crear el ClientLoan
        ClientLoan clientLoan = createClientLoan(loanAplicationDTO);

        clientLoan.setClient(client);  // Establecer el cliente en el ClientLoan
        clientLoan.setLoan(loan);  // Establecer el préstamo en el ClientLoan

        // Agregar el ClientLoan al cliente
        clientService.addClientLoanToClient(client, clientLoan);

        // Agregar el ClientLoan al préstamo
        loan.addClientLoans(clientLoan);



        // Crear la transacción para el préstamo
        Transaction transaction = transactionService.createTransactionToLoan(loanAplicationDTO);

        // Añadir la transacción a la cuenta
        accountService.addTransactionToAccount(account, transaction);

        // Aumentar el saldo de la cuenta
        accountService.addAmountToAccount(account, loanAplicationDTO.amount());

        // Guardar los cambios en la base de datos

        clientLoanRepository.save(clientLoan);
        clientRepository.save(client); // Asegúrate de guardar el cliente
        loanRepository.save(loan); // Asegúrate de guardar el préstamo
        transactionService.saveTransaction(transaction); // Guardar la transacción
        accountService.saveAccount(account); // Guardar la cuenta

        return loan;
    }



}
