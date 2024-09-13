package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> allAccounts();

    List<AccountDTO> transformAccountsToAccountsDTO(List<Account> accountList);

    List<AccountDTO> getClientAccounts(Authentication authentication);

    AccountDTO getAccountById(Long id);

    String generateAndValidateAccountNumber();

    Account createAccount();

    void maxAccountsNotExceeded(Client client);

    Account addAndSaveAccount(Authentication authentication);

    Account findAccountByNumber(String string);

    Boolean existAccountNumber(String accountNumber);

    String createAccountNumber();

    void saveAccount(Account account);

    void addTransactionToAccount(Account account, Transaction transaction);

    void addAmountToAccount(Account account, double amount );

    void subtractAmountToAccount(Account account, double amount);



}
