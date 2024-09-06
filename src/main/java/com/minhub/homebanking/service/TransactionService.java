package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    Transaction createTransaction(MakeTransactionDTO makeTransactionDTO, Authentication authentication);

    void validateTransactionInputs(MakeTransactionDTO makeTransactionDTO);

    void validateAccountExists(Account account, String accountType);

    void validateAccountOwnership(Account account, Client client);

    void validateSufficientBalance(Account account, Double amount);

    Transaction createTransaction(MakeTransactionDTO makeTransactionDTO, Account transactionAccount, Account destinationAccount);

    void updateAccountBalances(MakeTransactionDTO makeTransactionDTO, Account transactionAccount, Account destinationAccount);
}
