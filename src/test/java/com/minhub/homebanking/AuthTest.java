package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.LoginDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.service.ClientService;
import com.minhub.homebanking.servicesSecurity.JwtUtilService;
import com.minhub.homebanking.utils.AccountNumberGenerator;
import com.minhub.homebanking.utils.AuthValidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class AuthTest {



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

    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl();

        // Create and save a client with a password for login tests
        Client client = new Client("John", "Doe", "john.doe@gmail.com", passwordEncoder.encode("password123"));
        clientRepository.save(client);
    }

    @Test
    public void register_ShouldCreateClientSuccessfully() {
        RegisterDTO registerDTO = new RegisterDTO("Jane", "Doe", "jane.doe@gmail.com", "password123");

        ResponseEntity<?> response = authService.register(registerDTO);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is("Client registered successfully"));

        Client registeredClient = clientRepository.findByEmail("jane.doe@gmail.com");
        assertThat(registeredClient, notNullValue());
        assertThat(registeredClient.getEmail(), is("jane.doe@gmail.com"));
    }

    @Test
    public void register_ShouldReturnBadRequest_WhenEmailIsEmpty() {
        RegisterDTO registerDTO = new RegisterDTO("Jane", "Doe", "", "password123");

        ResponseEntity<?> response = authService.register(registerDTO);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), is("The email field cannot be empty"));
    }

    @Test
    public void register_ShouldReturnBadRequest_WhenEmailExists() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@gmail.com", "password123");

        ResponseEntity<?> response = authService.register(registerDTO);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), is("Email already exists"));
    }

    @Test
    public void login_ShouldReturnJwtToken_WhenCredentialsAreValid() {
        LoginDTO loginDTO = new LoginDTO("john.doe@gmail.com", "password123");

        ResponseEntity<?> response = authService.login(loginDTO);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), instanceOf(String.class));
    }

    @Test
    public void login_ShouldReturnBadRequest_WhenCredentialsAreInvalid() {
        LoginDTO loginDTO = new LoginDTO("john.doe@gmail.com", "wrongpassword");

        ResponseEntity<?> response = authService.login(loginDTO);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), is("Email or password invalid"));
    }

    @Test
    public void getCurrentClient_ShouldReturnClientDTO() {
        Authentication authentication = createMockAuthentication("john.doe@gmail.com");

        ClientDTO clientDTO = authService.getCurrentClient(authentication);

        assertThat(clientDTO, notNullValue());
        assertThat(clientDTO.getEmail(), is("john.doe@gmail.com"));
    }

    // Método auxiliar para crear una autenticación simulada
    private Authentication createMockAuthentication(String email) {
        return new Authentication() {
            @Override
            public String getName() {
                return email;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public Object getPrincipal() {
                return email;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public List<GrantedAuthority> getAuthorities() {
                return null;
            }
        };
    }
}
