package com.minhub.homebanking;

import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.*;
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
									  CardRepository cardRepository) {
		return (args) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tomorrow = now.plusDays(1);

			// Crear clientes
			Client client1 = new Client("Melba", "Morel", "melbamorel@hotmail.com", passwordEncoder.encode("melba123"));
			Client client2 = new Client("Fran", "Paez", "fran.paez@example.com", passwordEncoder.encode("melba123"));
			Client client3 = new Client("Pepe", "Pérez", "pepe.perez@example.com", passwordEncoder.encode("melba123"));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			// Crear cuentas para client1
			Account account1 = new Account("VIN001", 5000, now);
			Account account2 = new Account("VIN002", 7500, now);

			// Crear transacciones para account1 y account2


			client1.addAccount(account1);
			client1.addAccount(account2);

			accountRepository.save(account1);
			accountRepository.save(account2);

			// Crear cuentas para client2
			Account account3 = new Account("VIN003", 2000, now);
			Account account4 = new Account("VIN004", 3000, now);

			// Crear transacciones para account3 y account4


			client2.addAccount(account3);
			client2.addAccount(account4);

			accountRepository.save(account3);
			accountRepository.save(account4);

			// Crear cuentas para client3
			Account account5 = new Account("VIN005", 1000, now);
			Account account6 = new Account("VIN006", 4000, now);

			// Crear transacciones para account5 y account6


			client3.addAccount(account5);
			client3.addAccount(account6);

			accountRepository.save(account5);
			accountRepository.save(account6);

			// Crear préstamos
			Loan mortgage = new Loan("mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60), new HashSet<>());
			Loan personal = new Loan("personal", 100000, Arrays.asList(6, 12, 24), new HashSet<>());
			Loan automotive = new Loan("automotive", 300000, Arrays.asList(6, 12, 24, 36), new HashSet<>());

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
			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, cardNumberGenerated.generateCardNumber(), cvvGenerated.generateCVV(), now, now.plusYears(5));
			Card card2 = new Card(CardType.DEBIT, CardColor.TITANIUM, cardNumberGenerated.generateCardNumber(), cvvGenerated.generateCVV(), now, now.plusYears(5));
			client1.addCards(card1);
			client1.addCards(card2);

			cardRepository.save(card1);
			cardRepository.save(card2);

			Card card3 = new Card(CardType.DEBIT, CardColor.SILVER, cardNumberGenerated.generateCardNumber(), cvvGenerated.generateCVV(), now, now.plusYears(5));
			client2.addCards(card3);
			cardRepository.save(card3);
		};
	}

//	private void createAndSaveTransactions(Account account1, Account account2, TransactionRepository transactionRepository) {
//		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime tomorrow = now.plusDays(1);
//
//		// Crear transacciones para account1
//		Transaction transaction1_1 = new Transaction(5000, "Debit", now, TransactionType.CREDIT);
//		Transaction transaction1_2 = new Transaction(-500, "Retiro", tomorrow, TransactionType.DEBIT);
//		Transaction transaction1_3 = new Transaction(1500, "Depósito", now, TransactionType.CREDIT);
//
//		// Crear transacciones para account2
//		Transaction transaction2_1 = new Transaction(2000, "Depósito", now, TransactionType.CREDIT);
//		Transaction transaction2_2 = new Transaction(-1000, "Retiro", tomorrow, TransactionType.DEBIT);
//		Transaction transaction2_3 = new Transaction(2000, "Depósito", now, TransactionType.CREDIT);
//
//		// Agregar transacciones a las cuentas
//		account1.addTransaction(transaction1_1);
//		account1.addTransaction(transaction1_2);
//		account1.addTransaction(transaction1_3);
//
//		account2.addTransaction(transaction2_1);
//		account2.addTransaction(transaction2_2);
//		account2.addTransaction(transaction2_3);
//
//		// Guardar transacciones
//		transactionRepository.save(transaction1_1);
//		transactionRepository.save(transaction1_2);
//		transactionRepository.save(transaction1_3);
//		transactionRepository.save(transaction2_1);
//		transactionRepository.save(transaction2_2);
//		transactionRepository.save(transaction2_3);
//	}
}
