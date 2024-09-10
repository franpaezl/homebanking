package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.TransactionRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;


    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Override
    public void validateTransactionInputs(MakeTransactionDTO makeTransactionDTO) {
        if (makeTransactionDTO.transactionAccount().isBlank()) {
            throw new IllegalArgumentException("The transaction account field must not be empty");
        }
        if (makeTransactionDTO.destinationAccount().isBlank()) {
            throw new IllegalArgumentException("The destination account field must not be empty");
        }
        if (makeTransactionDTO.amount() == null || makeTransactionDTO.amount() <= 0) {
            throw new IllegalArgumentException("Enter a valid amount");
        }
        if (makeTransactionDTO.description().isBlank()) {
            throw new IllegalArgumentException("The description field must not be empty");
        }
        if (makeTransactionDTO.destinationAccount().equals(makeTransactionDTO.transactionAccount())) {
            throw new IllegalArgumentException("The transaction account and the destination account must not be the same");
        }
    }

    @Override
    public void validateAccountExists(Account account, String accountType) {
        if (account == null) {
            throw new IllegalArgumentException("The " + accountType + " account entered does not exist");
        }
    }

    @Override
    public void validateAccountOwnership(Account account, Client client) {
        if (!account.getOwner().equals(client)) {
            throw new IllegalArgumentException("The transaction account entered does not belong to the client");
        }
    }

    @Override
    public void validateSufficientBalance(Account account, Double amount) {
        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("You do not have sufficient balance to carry out the operation");
        }
    }

    @Override
    public Transaction createTransaction(MakeTransactionDTO makeTransactionDTO, TransactionType transactionType) {
        if (transactionType == TransactionType.DEBIT){
            return new Transaction(makeTransactionDTO.amount() * -1, makeTransactionDTO.description(), LocalDateTime.now(),transactionType);
        }
        else return new Transaction(makeTransactionDTO.amount(), makeTransactionDTO.description(), LocalDateTime.now(),transactionType);
    }

    @Override
    public Transaction createTransactionToLoan(LoanAplicationDTO loanAplicationDTO) {
        return new Transaction(loanAplicationDTO.amount(), "Solicit loan", LocalDateTime.now(), TransactionType.CREDIT);
    }

    @Override
    public Transaction makeTransaction(MakeTransactionDTO makeTransactionDTO, Authentication authentication) {
        // Obtener el cliente autenticado
        Client authenticatedClient = clientService.getAuthenticatedClient(authentication);

        // Obtener las cuentas de la transacción y destino
        Account transactionAccount = accountService.findAccountByNumber(makeTransactionDTO.transactionAccount());
        Account destinationAccount = accountService.findAccountByNumber(makeTransactionDTO.destinationAccount());

        // Validaciones
        validateTransactionInputs(makeTransactionDTO);
        validateAccountExists(transactionAccount, "Transaction");
        validateAccountExists(destinationAccount, "Destination");
        validateAccountOwnership(transactionAccount, authenticatedClient);
        validateSufficientBalance(transactionAccount, makeTransactionDTO.amount());

        // Crear y registrar la transacción
        Transaction debitTransaction = createTransaction(makeTransactionDTO, TransactionType.DEBIT);
        Transaction creditTransaction = createTransaction(makeTransactionDTO, TransactionType.CREDIT);

        transactionAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        transactionAccount.setBalance(transactionAccount.getBalance() - makeTransactionDTO.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDTO.amount());

        accountRepository.save(transactionAccount);
        accountRepository.save(destinationAccount);



        return debitTransaction;
    }
}
