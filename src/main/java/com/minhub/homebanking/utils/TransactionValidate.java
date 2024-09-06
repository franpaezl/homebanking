package com.minhub.homebanking.utils;

import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;

public class TransactionValidate {

    public static void validateTransactionInputs(MakeTransactionDTO makeTransactionDTO) {
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

    public static void validateAccountExists(Account account, String accountType) {
        if (account == null) {
            throw new IllegalArgumentException("The " + accountType + " account entered does not exist");
        }
    }

    public static void validateAccountOwnership(Account account, Client client) {
        if (!account.getOwner().equals(client)) {
            throw new IllegalArgumentException("The transaction account entered does not belong to the client");
        }
    }

    public static void validateSufficientBalance(Account account, Double amount) {
        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("You do not have sufficient balance to carry out the operation");
        }
    }
}
