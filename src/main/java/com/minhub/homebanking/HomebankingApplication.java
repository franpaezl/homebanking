package com.minhub.homebanking;

import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.*;
import com.minhub.homebanking.service.CardService;
import com.minhub.homebanking.utils.CVVGenerated;
import com.minhub.homebanking.utils.CardNumberGenerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CardNumberGenerated cardNumberGenerated;

    @Autowired
    private CVVGenerated cvvGenerated;

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository, CardService cardService) {
        return (args) -> {
            LocalDateTime now = LocalDateTime.now();

            // Crear clientes
            Client client1 = new Client("Melba", "Morel", "melbamorel@hotmail.com", passwordEncoder.encode("melba123"));
            Client client2 = new Client("Fran", "Paez", "fran.paez@admin.com", passwordEncoder.encode("melba123"));
            Client client3 = new Client("Pepe", "Pérez", "pepe.perez@example.com", passwordEncoder.encode("melba123"));

            clientRepository.save(client1);
            clientRepository.save(client2);
            clientRepository.save(client3);

            // Crear cuentas para client1
            Account account1 = new Account("VIN001", 5000, now);
            Account account2 = new Account("VIN002", 7500, now);
            client1.addAccount(account1);
            client1.addAccount(account2);
            accountRepository.save(account1);
            accountRepository.save(account2);

            // Crear y agregar transacciones para account1
            Transaction transaction1 = new Transaction(-2000, "New Laptop", now, TransactionType.DEBIT);
            account1.addTransaction(transaction1);
            transactionRepository.save(transaction1);

            Transaction transaction2 = new Transaction(5000, "Freelance Work", now, TransactionType.CREDIT);
            account1.addTransaction(transaction2);
            transactionRepository.save(transaction2);

            Transaction transaction3 = new Transaction(1000, "Gift", now, TransactionType.CREDIT);
            account1.addTransaction(transaction3);
            transactionRepository.save(transaction3);

            Transaction transaction4 = new Transaction(-500, "Utilities", now, TransactionType.DEBIT);
            account1.addTransaction(transaction4);
            transactionRepository.save(transaction4);

            Transaction transaction5 = new Transaction(1500, "Consulting", now, TransactionType.CREDIT);
            account1.addTransaction(transaction5);
            transactionRepository.save(transaction5);

            Transaction transaction6 = new Transaction(-300, "Groceries", now, TransactionType.DEBIT);
            account1.addTransaction(transaction6);
            transactionRepository.save(transaction6);

            // Crear cuentas para client2
            Account account3 = new Account("VIN003", 2000, now);
            Account account4 = new Account("VIN004", 3000, now);
            client2.addAccount(account3);
            client2.addAccount(account4);
            accountRepository.save(account3);
            accountRepository.save(account4);

            // Crear y agregar transacciones para account3
            Transaction transaction7 = new Transaction(-800, "Rent", now, TransactionType.DEBIT);
            account3.addTransaction(transaction7);
            transactionRepository.save(transaction7);

            Transaction transaction8 = new Transaction(2500, "Salary", now, TransactionType.CREDIT);
            account3.addTransaction(transaction8);
            transactionRepository.save(transaction8);

            Transaction transaction9 = new Transaction(1200, "Bonus", now, TransactionType.CREDIT);
            account3.addTransaction(transaction9);
            transactionRepository.save(transaction9);

            Transaction transaction10 = new Transaction(-400, "Internet", now, TransactionType.DEBIT);
            account3.addTransaction(transaction10);
            transactionRepository.save(transaction10);

            Transaction transaction11 = new Transaction(700, "Freelance Job", now, TransactionType.CREDIT);
            account3.addTransaction(transaction11);
            transactionRepository.save(transaction11);

            Transaction transaction12 = new Transaction(-150, "Transportation", now, TransactionType.DEBIT);
            account3.addTransaction(transaction12);
            transactionRepository.save(transaction12);

            // Crear cuentas para client3
            Account account5 = new Account("VIN005", 1000, now);
            Account account6 = new Account("VIN006", 4000, now);
            client3.addAccount(account5);
            client3.addAccount(account6);
            accountRepository.save(account5);
            accountRepository.save(account6);

            // Crear y agregar transacciones para account5
            Transaction transaction13 = new Transaction(-200, "Dinner", now, TransactionType.DEBIT);
            account5.addTransaction(transaction13);
            transactionRepository.save(transaction13);

            Transaction transaction14 = new Transaction(1800, "Part-time Work", now, TransactionType.CREDIT);
            account5.addTransaction(transaction14);
            transactionRepository.save(transaction14);

            Transaction transaction15 = new Transaction(900, "Freelance Project", now, TransactionType.CREDIT);
            account5.addTransaction(transaction15);
            transactionRepository.save(transaction15);

            Transaction transaction16 = new Transaction(-300, "Gym Membership", now, TransactionType.DEBIT);
            account5.addTransaction(transaction16);
            transactionRepository.save(transaction16);

            Transaction transaction17 = new Transaction(500, "Gift Received", now, TransactionType.CREDIT);
            account5.addTransaction(transaction17);
            transactionRepository.save(transaction17);

            Transaction transaction18 = new Transaction(-400, "Shopping", now, TransactionType.DEBIT);
            account5.addTransaction(transaction18);
            transactionRepository.save(transaction18);

            // Crear préstamos
            Loan mortgage = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60));
            Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
            Loan automotive = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24, 36));

            loanRepository.save(mortgage);
            loanRepository.save(personal);
            loanRepository.save(automotive);

            // Crear préstamos de clientes
            ClientLoan clientLoan1 = new ClientLoan(400000, 60);
            client1.addClientLoans(clientLoan1);
            mortgage.addClientLoans(clientLoan1);

            ClientLoan clientLoan2 = new ClientLoan(50000, 12);
            client1.addClientLoans(clientLoan2);
            personal.addClientLoans(clientLoan2);

            ClientLoan clientLoan3 = new ClientLoan(100000, 24);
            client2.addClientLoans(clientLoan3);
            personal.addClientLoans(clientLoan3);

            ClientLoan clientLoan4 = new ClientLoan(200000, 36);
            client2.addClientLoans(clientLoan4);
            automotive.addClientLoans(clientLoan4);

            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);

            // Crear tarjetas
            Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, cardNumberGenerated.generateCardNumber(), cardService.generateCVV(), now, now.plusYears(5));
            Card card2 = new Card(CardType.DEBIT, CardColor.TITANIUM, cardNumberGenerated.generateCardNumber(), cardService.generateCVV(), now, now.plusYears(5));
            client1.addCards(card1);
            client1.addCards(card2);
            cardRepository.save(card1);
            cardRepository.save(card2);

            Card card3 = new Card(CardType.DEBIT, CardColor.SILVER, cardNumberGenerated.generateCardNumber(), cardService.generateCVV(), now, now.plusYears(5));
            client2.addCards(card3);
            cardRepository.save(card3);
        };
    }
}
