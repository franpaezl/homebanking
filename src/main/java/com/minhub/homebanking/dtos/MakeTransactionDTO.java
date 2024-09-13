package com.minhub.homebanking.dtos;

public record MakeTransactionDTO(String originAccount, String destinationAccount, Double amount, String description) {
}
