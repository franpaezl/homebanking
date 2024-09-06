package com.minhub.homebanking.dtos;

public record LoanAplicationDTO(Long id, int payments, double amount, String account) {
}
