package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.utils.AccountNumberGenerator;
import com.minhub.homebanking.utils.AccountValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AccountsServiceImpl implements AccountService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountValidate accountValidate;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Override
    public List<Account> allAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> transformAccountsToAccountsDTO(List<Account> accountList) {
        return accountList.stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public Optional<AccountDTO> getAccountById(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new);
    }

    @Override
    public void validateMaxAccountsNotExceeded(Client client) {
        accountValidate.maxAccountsNotExceeded(client);
    }

    @Override
    public Account createNewAccount() {
        return new Account(accountNumberGenerator.makeAccountNumber(), 0.0, LocalDateTime.now());
    }

    @Override
    public Account addAndSaveAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        validateMaxAccountsNotExceeded(client);
        Account newAccount = createNewAccount();

        client.addAccount(newAccount);
        accountRepository.save(newAccount);
        clientRepository.save(client);

        return newAccount;  // Return the new account or adjust the method signature if needed
    }

    @Override
    public List<AccountDTO> getClientAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> clientAccounts = accountRepository.findByOwner(client);
        return transformAccountsToAccountsDTO(clientAccounts);
    }
}
