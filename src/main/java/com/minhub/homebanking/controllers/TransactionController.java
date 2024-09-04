package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> makeTransaction(@RequestBody MakeTransactionDTO makeTransactionDTO, Authentication authentication) {
        try {
            Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
            Account transactionAccount = accountRepository.findByAccountNumber(makeTransactionDTO.transactionAccount());
            Account destinationAccount = accountRepository.findByAccountNumber(makeTransactionDTO.destinationAccount());

            // Verificaciones iniciales
            if (makeTransactionDTO.transactionAccount().isBlank( )) {
                return new ResponseEntity<>("The transaction account field must not be empty", HttpStatus.FORBIDDEN);
            }
            if (makeTransactionDTO.destinationAccount().isBlank()) {
                return new ResponseEntity<>("The destination account field must not be empty", HttpStatus.FORBIDDEN);
            }
            if (makeTransactionDTO.amount() == null || makeTransactionDTO.amount() <= 0) {
                return new ResponseEntity<>("Enter a valid amount", HttpStatus.FORBIDDEN);
            }
            if (makeTransactionDTO.description().isBlank()) {
                return new ResponseEntity<>("The description field must not be empty", HttpStatus.FORBIDDEN);
            }
            if (makeTransactionDTO.destinationAccount().equals(makeTransactionDTO.transactionAccount())) {
                return new ResponseEntity<>("The transaction account and the destination account must not be the same", HttpStatus.FORBIDDEN);
            }

            // Verificación de la existencia de cuentas
            if (transactionAccount == null) {
                return new ResponseEntity<>("The transaction account entered does not exist", HttpStatus.FORBIDDEN);
            }
            if (destinationAccount == null) {
                return new ResponseEntity<>("The destination account entered does not exist", HttpStatus.FORBIDDEN);
            }

            // Verificación de propiedad de la cuenta
            if (!accountRepository.existsByIdAndOwner(transactionAccount.getId(), authenticatedClient)) {
                return new ResponseEntity<>("The transaction account entered does not belong to the client", HttpStatus.FORBIDDEN);
            }

            // Verificación de balance suficiente
            if (makeTransactionDTO.amount() > transactionAccount.getBalance()) {
                return new ResponseEntity<>("You do not have sufficient balance to carry out the operation", HttpStatus.FORBIDDEN);
            }

            // Creación y registro de las transacciones
            Transaction debitTransaction = new Transaction(-makeTransactionDTO.amount(), makeTransactionDTO.description(), LocalDateTime.now(), TransactionType.DEBIT);
            Transaction creditTransaction = new Transaction(makeTransactionDTO.amount(), makeTransactionDTO.description(), LocalDateTime.now(), TransactionType.CREDIT);

            transactionAccount.addTransaction(debitTransaction);
            destinationAccount.addTransaction(creditTransaction);

            transactionRepository.save(debitTransaction);
            transactionRepository.save(creditTransaction);

            // Actualización de saldos
            transactionAccount.setBalance(transactionAccount.getBalance() - makeTransactionDTO.amount());
            destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDTO.amount());

            accountRepository.save(transactionAccount);
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
