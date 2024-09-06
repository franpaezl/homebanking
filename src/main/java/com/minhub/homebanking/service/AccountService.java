package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> allAccounts();

    List<AccountDTO> transformAccountsToAccountsDTO(List<Account> accountList);

    Optional<AccountDTO> getAccountById(Long id);

    void validateMaxAccountsNotExceeded(Client client);

    Account createNewAccount();

    Account addAndSaveAccount(Authentication authentication);

    List<AccountDTO> getClientAccounts(Authentication authentication);


}
