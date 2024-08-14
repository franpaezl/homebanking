package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api/account")
public class AccountController {
    @Autowired
     private AccountRepository accountRepository;


    @GetMapping("/")
    public List<AccountDTO> getAllClients() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());

    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getById(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
