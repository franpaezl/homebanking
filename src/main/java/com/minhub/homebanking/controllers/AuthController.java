package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.LoginDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(Authentication authentication) {
        ClientDTO clientDTO = authService.getCurrentClient(authentication);
        return ResponseEntity.ok(clientDTO);
    }
}
