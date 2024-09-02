package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Boolean existsByCardNumber(String cardNumber);

    Boolean existsByCvv(String cvv);
}


