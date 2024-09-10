package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> makeTransaction(@RequestBody MakeTransactionDTO makeTransactionDTO, Authentication authentication) {
        try {
            // Llamar al método del servicio para crear la transacción
            Transaction transaction = transactionService.makeTransaction(makeTransactionDTO, authentication);

            // Devolver una respuesta de éxito
            return new ResponseEntity<>("Transaction completed successfully. Transaction ID: " + transaction.getId(), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Devolver una respuesta de error por parámetros inválidos
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);

        } catch (Exception e) {
            // Devolver una respuesta de error por excepción general
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
