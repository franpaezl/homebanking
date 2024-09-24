package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    boolean existsByClientAndLoanId(Client client, Long loanId);
}

