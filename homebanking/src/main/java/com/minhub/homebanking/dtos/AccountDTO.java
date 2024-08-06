package com.minhub.homebanking.dtos;



import com.minhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {


    private long id;
    private String accountNumber;
    private double balance;
    private LocalDate date;

    public AccountDTO(){

    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.date = account.getDate();
        this.accountNumber = account.getAccountNumber();
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

    public LocalDate getDate() {
        return date;
    }
}
