package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcountRepository extends JpaRepository<Account, Long> {
}
