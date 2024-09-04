package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

//    @GetMapping("/")
//    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
//        List<Account> getAllAccounts = accountRepository.findAll();
//        List<AccountDTO> allAccountsDto = getAllAccounts.stream().map(AccountDTO::new).collect(Collectors.toList());
//        return new ResponseEntity<>(allAccountsDto, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
//        Optional<Account> accountById = accountRepository.findById(id);
//        if (accountById.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            Account accountData = accountById.get();
//            AccountDTO accountDTO = new AccountDTO(accountData);
//            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
//        }
//    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        try {
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client.getAccounts().size() == 3) {
                return new ResponseEntity<>("You cannot create a new account at this time. You have reached the maximum number of allowed accounts (3)", HttpStatus.FORBIDDEN);
            }

            Account newAccount = new Account(accountNumberGenerator.makeAccountNumber(), 00.0, LocalDateTime.now());
            client.addAccount(newAccount);
            accountRepository.save(newAccount);
            clientRepository.save(client);

            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<?> getClientAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> clientAccounts = accountRepository.findByOwner(client);
        List<AccountDTO> clientAccountDTO = clientAccounts.stream().map(AccountDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(clientAccountDTO, HttpStatus.OK);
    }
}
