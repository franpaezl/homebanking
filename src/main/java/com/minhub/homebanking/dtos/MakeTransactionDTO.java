package com.minhub.homebanking.dtos;

public record  MakeTransactionDTO(String transactionAccount, String destinationAccount, Double amount, String description) {
}
