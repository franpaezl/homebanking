package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.LoginDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.service.AuthService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.servicesSecurity.JwtUtilService;
import com.minhub.homebanking.utils.AccountNumberGenerator;
import com.minhub.homebanking.utils.AuthValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private AuthValidate authValidate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Override
    public void authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    }

    @Override
    public UserDetails getUserDetails(String email) {
        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String getJWT(UserDetails userDetails) {
        return jwtUtilService.generateToken(userDetails);
    }

    @Override
    public void registerValidate(RegisterDTO registerDTO) {
        if (registerDTO.email().isBlank()) {
            throw new IllegalArgumentException("The email field cannot be empty");
        }
        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (registerDTO.firstName().isBlank()) {
            throw new IllegalArgumentException("First name and  cannot be empty");
        }

        if (registerDTO.lastName().isBlank()) {
            throw new IllegalArgumentException("Last name and  cannot be empty");
        }

        if(registerDTO.password().isBlank()|| registerDTO.password() == null){
            throw new IllegalArgumentException("Insert password");
        }

        if (registerDTO.password().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }



    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        try {
            // Validar los datos del registro
            registerValidate(registerDTO);


            Account newAccount = accountService.createAccount();
            accountService.saveAccount(newAccount);

            // Crear nuevo cliente y asociar la cuenta
            Client newClient = clientService.createNewClient(registerDTO);
            clientService.addAccountToClient(newClient,newAccount);
            clientService.saveClient(newClient);

            return new ResponseEntity<>("Client registered successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error registering client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try {
            authenticate(loginDTO.email(), loginDTO.password());
            UserDetails userDetails = getUserDetails(loginDTO.email());
            String jwt = getJWT(userDetails);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return new ResponseEntity<>("Email or password invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ClientDTO getCurrentClient(Authentication authentication) {
        Client client = clientService.getAuthenticatedClient(authentication);
        return new ClientDTO(client);
    }
}
