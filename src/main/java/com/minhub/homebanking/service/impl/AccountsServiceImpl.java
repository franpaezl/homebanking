package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountsServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountNumberGenerator accountNumberGenerator;

    @Autowired
    ClientService clientService;

    @Override
    public List<Account> allAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> transformAccountsToAccountsDTO(List<Account> accountList) {
        return accountList.stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getClientAccounts(Authentication authentication) {
        Client client = clientService.getAuthenticatedClient(authentication);
        List<Account> clientAccounts = accountRepository.findByOwner(client);
        return transformAccountsToAccountsDTO(clientAccounts);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public String createAccountNumber() {
        return "VIN-" + accountNumberGenerator.generateEightDigitNumber();
    }


    @Override
    public Boolean existAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);

    }


    @Override
    public String generateAndValidateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = createAccountNumber();
        } while (existAccountNumber(accountNumber));

        return accountNumber;
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void addTransactionToAccount(Account account, Transaction transaction) {
        account.addTransaction(transaction);
    }

    @Override
    public void addAmountToAccount(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
    }

    @Override
    public void subtractAmountToAccount(Account account, double amount) {
        account.setBalance(account.getBalance() - amount);
    }


    @Override
    public Account createAccount() {
        return new Account(generateAndValidateAccountNumber(), 0, LocalDateTime.now());
    }

    @Override
    public void maxAccountsNotExceeded(Client client) {
        if (client.getAccounts().size() == 3) {
            throw new IllegalArgumentException("You cannot create a new account at this time. You have reached the maximum number of allowed accounts (3)");
        }
    }


    @Override
    public Account addAndSaveAccount(Authentication authentication) {
        Client client = clientService.getAuthenticatedClient(authentication);

        maxAccountsNotExceeded(client);

        Account newAccount = createAccount();

        client.addAccount(newAccount);

        accountRepository.save(newAccount);

        clientService.saveClient(client);

        return newAccount;
    }

    @Override
    public Account findAccountByNumber(String string) {
        return accountRepository.findByAccountNumber(string);
    }
}
