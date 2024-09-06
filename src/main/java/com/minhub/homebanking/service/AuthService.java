package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.LoginDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    void authenticate(String email, String password);

    UserDetails getUserDetails(String email);

    String getJWT(UserDetails userDetails);

    void registerValidate(RegisterDTO registerDTO);

    String encodedPassword(RegisterDTO registerDTO);

    Account createNewAccount();

    Client createNewClient(RegisterDTO registerDTO);

    ResponseEntity<?> register(RegisterDTO registerDTO);

    ResponseEntity<?> login(LoginDTO loginDTO);

    ClientDTO getCurrentClient(Authentication authentication);
}
