package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.TransactionRepository;
import com.minhub.homebanking.service.TransactionService;
import com.minhub.homebanking.utils.TransactionValidate;
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

    @Override
    public void validateTransactionInputs(MakeTransactionDTO makeTransactionDTO) {
        TransactionValidate.validateTransactionInputs(makeTransactionDTO);
    }

    @Override
    public void validateAccountExists(Account account, String accountType) {
        TransactionValidate.validateAccountExists(account, accountType);
    }

    @Override
    public void validateAccountOwnership(Account account, Client client) {
        TransactionValidate.validateAccountOwnership(account, client);
    }

    @Override
    public void validateSufficientBalance(Account account, Double amount) {
        TransactionValidate.validateSufficientBalance(account, amount);
    }

    @Override
    public Transaction createTransaction(MakeTransactionDTO makeTransactionDTO, Account transactionAccount, Account destinationAccount) {
        Transaction debitTransaction = new Transaction(-makeTransactionDTO.amount(), makeTransactionDTO.description(), LocalDateTime.now(), TransactionType.DEBIT);
        Transaction creditTransaction = new Transaction(makeTransactionDTO.amount(), makeTransactionDTO.description(), LocalDateTime.now(), TransactionType.CREDIT);

        transactionAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return debitTransaction;
    }

    @Override
    public void updateAccountBalances(MakeTransactionDTO makeTransactionDTO, Account transactionAccount, Account destinationAccount) {
        transactionAccount.setBalance(transactionAccount.getBalance() - makeTransactionDTO.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDTO.amount());

        accountRepository.save(transactionAccount);
        accountRepository.save(destinationAccount);
    }

    @Override
    public Transaction createTransaction(MakeTransactionDTO makeTransactionDTO, Authentication authentication) {
        // Obtener el cliente autenticado
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        // Obtener las cuentas de la transacción y destino
        Account transactionAccount = accountRepository.findByAccountNumber(makeTransactionDTO.transactionAccount());
        Account destinationAccount = accountRepository.findByAccountNumber(makeTransactionDTO.destinationAccount());

        // Validaciones
        validateTransactionInputs(makeTransactionDTO);
        validateAccountExists(transactionAccount, "Transaction");
        validateAccountExists(destinationAccount, "Destination");
        validateAccountOwnership(transactionAccount, authenticatedClient);
        validateSufficientBalance(transactionAccount, makeTransactionDTO.amount());

        // Crear y registrar la transacción
        Transaction debitTransaction = createTransaction(makeTransactionDTO, transactionAccount, destinationAccount);

        // Actualizar los balances de las cuentas
        updateAccountBalances(makeTransactionDTO, transactionAccount, destinationAccount);

        return debitTransaction;
    }
}
