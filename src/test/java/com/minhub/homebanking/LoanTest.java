package com.minhub.homebanking;

import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.ClientLoan;
import com.minhub.homebanking.models.Loan;
import com.minhub.homebanking.service.impl.LoanServiceImpl;
import com.minhub.homebanking.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class LoanServiceImplTest {

    @Mock
    private Account account;

    @Mock
    private Client client;

    @Mock
    private Loan loan;

    @Mock
    private TransactionServiceImpl transactionService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoanServiceImpl loanService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para verificatedLoan
    @Test
    public void verificatedLoan_ShouldThrowException_ForBlankAccountNumber() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, ""); // cuenta vacía

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, loan);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The transaction account field must not be empty"));
    }

    @Test
    public void verificatedLoan_ShouldThrowException_ForNonExistingAccount() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456");

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, null, loan); // cuenta nula
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The specified account does not exist."));
    }

    @Test
    public void verificatedLoan_ShouldThrowException_ForNonBelongingAccount() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456");

        when(client.getAccounts()).thenReturn(List.of(new Account("654321"))); // simular cuenta del cliente

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, loan);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The specified account does not belong to the authenticated client."));
    }

    @Test
    public void verificatedLoan_ShouldThrowException_ForInvalidLoanType() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456");

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, null); // préstamo nulo
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Loan type not found."));
    }

    @Test
    public void verificatedLoan_ShouldThrowException_ForAmountBelowMinimum() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(3000.0, 12, "123456"); // monto menor al mínimo

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, loan);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The loan amount does not meet the minimum amount required."));
    }

    @Test
    public void verificatedLoan_ShouldThrowException_ForAmountExceedingMaximum() {
        when(loan.getMaxAmount()).thenReturn(15000.0);
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456");

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, loan);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The loan amount exceeds the maximum amount allowed."));
    }

    @Test
    public void verificatedLoan_ShouldThrowException_ForInvalidNumberOfPayments() {
        when(loan.getPayment()).thenReturn(List.of(24, 36)); // pagos permitidos 24 y 36
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456"); // 12 pagos

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, loan);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The number of payments is not valid for the selected loan."));
    }

    @Test
    public void verificatedLoan_ShouldPass_ForValidInputs() {
        when(loan.getMaxAmount()).thenReturn(25000.0);
        when(loan.getPayment()).thenReturn(List.of(12, 24, 36));
        when(client.getAccounts()).thenReturn(List.of(account));
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456");

        Exception exception = null;
        try {
            loanService.verificatedLoan(loanAplicationDTO, client, account, loan);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, nullValue()); // no debería lanzarse ninguna excepción
    }

    // Tests para addPercent
    @Test
    public void addPercent_ShouldReturn15Percent_ForLessThan12Payments() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(10000.0, 6, "123456");

        double result = loanService.addPercent(loanAplicationDTO);

        assertThat(result, is(10000.0 * 0.15));
    }

    @Test
    public void addPercent_ShouldReturn20Percent_For12Payments() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(10000.0, 12, "123456");

        double result = loanService.addPercent(loanAplicationDTO);

        assertThat(result, is(10000.0 * 0.20));
    }

    @Test
    public void addPercent_ShouldReturn25Percent_ForMoreThan12Payments() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(10000.0, 24, "123456");

        double result = loanService.addPercent(loanAplicationDTO);

        assertThat(result, is(10000.0 * 0.25));
    }

    // Tests para createClientLoan
    @Test
    public void createClientLoan_ShouldReturnClientLoan_WithCorrectAmountAndPayments() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(, 12, 123456, "VIN001");

        ClientLoan clientLoan = loanService.createClientLoan(loanAplicationDTO);

        assertThat(clientLoan.getAmount(), is(loanService.getAmountWithPercent(loanAplicationDTO)));
        assertThat(clientLoan.getPayments(), is(12));
    }

    // Test para processLoanApplication
    @Test
    public void processLoanApplication_ShouldReturnLoan_ForValidInputs() {
        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(20000.0, 12, "123456");

        when(clientService.getAuthenticatedClient(authentication)).thenReturn(client);
        when(accountService.findAccountByNumber(loanAplicationDTO.accountNumber())).thenReturn(account);
        when(loanRepository.findById(loanAplicationDTO.id())).thenReturn(java.util.Optional.of(loan));

        Loan processedLoan = loanService.processLoanApplication(loanAplicationDTO, authentication);

        assertThat(processedLoan, is(notNullValue()));
    }
}