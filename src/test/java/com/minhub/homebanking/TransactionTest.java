package com.minhub.homebanking;

import com.minhub.homebanking.dtos.MakeTransactionDTO;
import com.minhub.homebanking.dtos.LoanAplicationDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TransactionTest {

    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionServiceImpl();
    }

    // Test for validateTransactionInputs
    @Test
    public void validateTransactionInputs_ShouldThrowException_ForBlankOriginAccount() {
        MakeTransactionDTO dto = new MakeTransactionDTO("", "123456", 100.0, "Test");

        Exception exception = null;
        try {
            transactionService.validateTransactionInputs(dto);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The origin account field must not be empty"));
    }

    @Test
    public void validateTransactionInputs_ShouldThrowException_ForBlankDestinationAccount() {
        MakeTransactionDTO dto = new MakeTransactionDTO("123456", "", 100.0, "Test");

        Exception exception = null;
        try {
            transactionService.validateTransactionInputs(dto);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The destination account field must not be empty"));
    }

    @Test
    public void validateTransactionInputs_ShouldThrowException_ForNegativeAmount() {
        MakeTransactionDTO dto = new MakeTransactionDTO("123456", "654321", -50.0, "Test");

        Exception exception = null;
        try {
            transactionService.validateTransactionInputs(dto);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Enter a valid amount"));
    }

    @Test
    public void validateTransactionInputs_ShouldThrowException_ForSameAccounts() {
        MakeTransactionDTO dto = new MakeTransactionDTO("123456", "123456", 100.0, "Test");

        Exception exception = null;
        try {
            transactionService.validateTransactionInputs(dto);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The origin account and the destination account must not be the same"));
    }

    @Test
    public void validateTransactionInputs_ShouldPass_ForValidInput() {
        MakeTransactionDTO dto = new MakeTransactionDTO("123456", "654321", 100.0, "Test");

        Exception exception = null;
        try {
            transactionService.validateTransactionInputs(dto);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, nullValue());  // No exception should be thrown
    }

    // Test for validateAccountExists
    @Test
    public void validateAccountExists_ShouldThrowException_ForNullAccount() {
        Exception exception = null;
        try {
            transactionService.validateAccountExists(null, "Origin");
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The Origin account entered does not exist"));
    }

    @Test
    public void validateAccountExists_ShouldPass_ForNonNullAccount() {
        Account account = new Account(); // Assuming Account has a default constructor

        Exception exception = null;
        try {
            transactionService.validateAccountExists(account, "Origin");
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, nullValue());  // No exception should be thrown
    }

    // Test for validateAccountOwnership
    @Test
    public void validateAccountOwnership_ShouldThrowException_ForNonMatchingClient() {
        Client client1 = new Client();  // Assuming Client has a default constructor
        Client client2 = new Client();  // Another client
        Account account = new Account();
        account.setOwner(client1);  // Set owner as client1

        Exception exception = null;
        try {
            transactionService.validateAccountOwnership(account, client2);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("The origin account entered does not belong to the client"));
    }

    @Test
    public void validateAccountOwnership_ShouldPass_ForMatchingClient() {
        Client client = new Client();
        Account account = new Account();
        account.setOwner(client);  // Set owner as the client

        Exception exception = null;
        try {
            transactionService.validateAccountOwnership(account, client);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, nullValue());  // No exception should be thrown
    }

    // Test for validateSufficientBalance
    @Test
    public void validateSufficientBalance_ShouldThrowException_ForInsufficientBalance() {
        Account account = new Account();
        account.setBalance(50.0);

        Exception exception = null;
        try {
            transactionService.validateSufficientBalance(account, 100.0);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("You do not have sufficient balance to carry out the operation"));
    }

    @Test
    public void validateSufficientBalance_ShouldPass_ForSufficientBalance() {
        Account account = new Account();
        account.setBalance(150.0);

        Exception exception = null;
        try {
            transactionService.validateSufficientBalance(account, 100.0);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception, nullValue());  // No exception should be thrown
    }

    // Test for createTransaction
    @Test
    public void createTransaction_ShouldCreateDebitTransaction() {
        MakeTransactionDTO dto = new MakeTransactionDTO("123456", "654321", 100.0, "Test");

        Transaction transaction = transactionService.createTransaction(dto, TransactionType.DEBIT);

        assertThat(transaction.getAmount(), is(-100.0));
        assertThat(transaction.getDescription(), is("Test"));
        assertThat(transaction.getType(), is(TransactionType.DEBIT));
        assertThat(transaction.getDate(), notNullValue());  // Check if the date is set
    }

    @Test
    public void createTransaction_ShouldCreateCreditTransaction() {
        MakeTransactionDTO dto = new MakeTransactionDTO("123456", "654321", 100.0, "Test");

        Transaction transaction = transactionService.createTransaction(dto, TransactionType.CREDIT);

        assertThat(transaction.getAmount(), is(100.0));
        assertThat(transaction.getDescription(), is("Test"));
        assertThat(transaction.getType(), is(TransactionType.CREDIT));
        assertThat(transaction.getDate(), notNullValue());  // Check if the date is set
    }


}
