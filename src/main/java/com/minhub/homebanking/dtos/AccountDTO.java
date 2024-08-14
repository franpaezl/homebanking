package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String accountNumber;
    private double balance;
    private LocalDateTime date;
    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.date = account.getDate();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}