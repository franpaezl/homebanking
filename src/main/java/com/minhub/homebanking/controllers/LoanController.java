package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/loans")
public class LoanController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private LoanService loanService;

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loanDTOs = loanService.getAllLoans();
        return ResponseEntity.ok(loanDTOs);
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> solicitLoan(@RequestBody LoanAplicationDTO loanAplicationDTO, Authentication authentication) {
        try {
            Client client = clientRepository.findByEmail(authentication.getName());
            loanService.processLoanApplication(loanAplicationDTO, client);
            return ResponseEntity.ok("Loan application approved.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
