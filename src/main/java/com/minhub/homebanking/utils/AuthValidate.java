package com.minhub.homebanking.utils;

import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthValidate {

    @Autowired
    private ClientRepository clientRepository;

    public void validateRegisterDTO(RegisterDTO registerDTO) {
        if (registerDTO.email().isBlank()) {
            throw new IllegalArgumentException("The email field cannot be empty");
        }
        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (registerDTO.firstName().isBlank() || registerDTO.lastName().isBlank()) {
            throw new IllegalArgumentException("First name and last name cannot be empty");
        }
        if (registerDTO.password().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }
}
