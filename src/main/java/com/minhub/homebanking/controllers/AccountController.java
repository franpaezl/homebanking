package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.AccountService;
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
    private AccountService accountService;

    @Autowired ClientRepository clientRepository;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<Account> getAllAccounts = accountService.allAccounts();
        List<AccountDTO> allAccountsDto = accountService.transformAccountsToAccountsDTO(getAllAccounts);
        return new ResponseEntity<>(allAccountsDto, HttpStatus.OK);
    }

    @GetMapping("accounts/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getAccountById(id);
        if (accountDTO != null) {
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


        @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        try {
            Account newAccount = accountService.addAndSaveAccount(authentication);
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<?> getClientAccounts(Authentication authentication) {
        try {
            List<AccountDTO> clientAccountDTO = accountService.getClientAccounts(authentication);
            return new ResponseEntity<>(clientAccountDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving accounts: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
