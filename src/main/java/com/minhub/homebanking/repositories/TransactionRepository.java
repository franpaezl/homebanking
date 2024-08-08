package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
