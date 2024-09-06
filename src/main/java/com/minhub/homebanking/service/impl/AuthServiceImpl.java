package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.LoginDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.AuthService;
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
    private AccountNumberGenerator accountNumberGenerator;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

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
        authValidate.validateRegisterDTO(registerDTO);
    }

    @Override
    public String encodedPassword(RegisterDTO registerDTO) {
        return passwordEncoder.encode(registerDTO.password());
    }

    @Override
    public Account createNewAccount() {
        return new Account(accountNumberGenerator.makeAccountNumber(), 0, LocalDateTime.now());
    }

    @Override
    public Client createNewClient(RegisterDTO registerDTO) {
        // Codifica la contraseña del cliente
        String encodedPassword = encodedPassword(registerDTO);

        // Crea un nuevo objeto Client con los datos proporcionados
        return new Client(
                registerDTO.firstName(),
                registerDTO.lastName(),
                registerDTO.email(),
                encodedPassword
        );
    }

    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        try {
            // Validar los datos del registro
            registerValidate(registerDTO);

            // Crear nueva cuenta
            Account newAccount = createNewAccount();
            accountRepository.save(newAccount);

            // Crear nuevo cliente y asociar la cuenta
            Client newClient = createNewClient(registerDTO);
            newClient.addAccount(newAccount);
            clientRepository.save(newClient);

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
        Client client = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }
}
