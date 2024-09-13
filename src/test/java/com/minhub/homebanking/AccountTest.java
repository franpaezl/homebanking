package com.minhub.homebanking;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.AccountService;
import com.minhub.homebanking.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class AccountTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client("John", "Doe", "john.doe@gmail.com", "password");
        clientService.saveClient(client);
    }

    @Test
    public void allAccounts_ShouldReturnAllAccounts() {
        Account account1 = new Account("VIN-12345678", 1000.0, LocalDateTime.now());
        Account account2 = new Account("VIN-87654321", 500.0, LocalDateTime.now());
        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountService.allAccounts();

        assertThat(accounts, hasSize(2));
        assertThat(accounts, containsInAnyOrder(account1, account2));
    }

    @Test
    public void createAccount_ShouldCreateAccountWithValidNumber() {
        Account newAccount = accountService.createAccount();

        assertThat(newAccount.getAccountNumber(), startsWith("VIN-"));
        assertThat(newAccount.getBalance(), is(0.0));
        assertThat(newAccount.getDate(), notNullValue());
    }

    @Test
    public void addAndSaveAccount_ShouldAddAccountToClient() {
        Authentication authentication = createMockAuthentication(client);  // Crea un mock simple de autenticación
        Account newAccount = accountService.addAndSaveAccount(authentication);

        Client updatedClient = clientRepository.findByEmail(client.getEmail());

        assertThat(newAccount.getAccountNumber(), startsWith("VIN-"));
        assertThat(updatedClient.getAccounts(), hasSize(1));
        assertThat(updatedClient.getAccounts(), contains(newAccount));
    }

    @Test
    public void maxAccountsNotExceeded_ShouldThrowException_WhenClientHasThreeAccounts() {
        client.addAccount(new Account("VIN-11111111", 0.0, LocalDateTime.now()));
        client.addAccount(new Account("VIN-22222222", 0.0, LocalDateTime.now()));
        client.addAccount(new Account("VIN-33333333", 0.0, LocalDateTime.now()));
        clientService.saveClient(client);

        IllegalArgumentException exception = null;

        try {
            accountService.maxAccountsNotExceeded(client);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("You cannot create a new account at this time. You have reached the maximum number of allowed accounts (3)"));
    }

    @Test
    public void generateAndValidateAccountNumber_ShouldGenerateUniqueAccountNumbers() {
        String accountNumber1 = accountService.generateAndValidateAccountNumber();
        String accountNumber2 = accountService.generateAndValidateAccountNumber();

        assertThat(accountNumber1, startsWith("VIN-"));
        assertThat(accountNumber2, startsWith("VIN-"));
        assertThat(accountNumber1, is(not(accountNumber2)));
    }

    @Test
    public void findAccountByNumber_ShouldReturnCorrectAccount() {
        Account account = new Account("VIN-12345678", 500.0, LocalDateTime.now());
        accountRepository.save(account);

        Account foundAccount = accountService.findAccountByNumber("VIN-12345678");

        assertThat(foundAccount, notNullValue());
        assertThat(foundAccount.getAccountNumber(), is("VIN-12345678"));
        assertThat(foundAccount.getBalance(), is(500.0));
    }

    @Test
    public void addAmountToAccount_ShouldIncreaseBalance() {
        Account account = new Account("VIN-12345678", 500.0, LocalDateTime.now());
        accountRepository.save(account);

        accountService.addAmountToAccount(account, 200.0);

        assertThat(account.getBalance(), is(700.0));
    }

    @Test
    public void subtractAmountToAccount_ShouldDecreaseBalance() {
        Account account = new Account("VIN-12345678", 500.0, LocalDateTime.now());
        accountRepository.save(account);

        accountService.subtractAmountToAccount(account, 200.0);

        assertThat(account.getBalance(), is(300.0));
    }

    // Método auxiliar para crear una autenticación simulada
    private Authentication createMockAuthentication(Client client) {
        return new Authentication() {
            @Override
            public String getName() {
                return client.getEmail();
            }

            // Otros métodos pueden dejarse vacíos o devolver valores por defecto si no son utilizados en los tests
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public Object getPrincipal() {
                return client;
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
