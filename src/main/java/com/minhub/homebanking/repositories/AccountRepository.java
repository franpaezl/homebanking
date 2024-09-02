package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByNumber(String number);
    List<Account> findByOwner (Client owner);
    Account findByAccount(String account);
}
