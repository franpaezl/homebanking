package com.minhub.homebanking;

import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository) {
		return (args) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tomorrow = now.plusDays(1);

			// Create clients
			Client client1 = new Client("Melba",  "melbamorel@hotmail.com","Morel", new HashSet<>(), new HashSet<>(),new HashSet<>());
			Client client2 = new Client("Fran",  "fran.paez@example.com","Paez", new HashSet<>(), new HashSet<>(),new HashSet<>());
			Client client3 = new Client("Pepe",  "pepe.perez@example.com","Pérez", new HashSet<>(), new HashSet<>(),new HashSet<>());

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			// Create Accounts and Transactions for client1
			Account account1 = new Account("VIN001", 5000, now, client1, new HashSet<>());
			Account account2 = new Account("VIN002", 7500, tomorrow, client1, new HashSet<>());

			Transaction transaction1_1 = new Transaction(1000, "Deposit", now, TransactionType.CREDIT, account1);
				Transaction transaction1_2 = new Transaction(-500, "Withdrawal", tomorrow, TransactionType.DEBIT, account1);
			Transaction transaction1_3 = new Transaction(1500, "Deposit", now, TransactionType.CREDIT, account1);

			Transaction transaction2_1 = new Transaction(2000, "Deposit", now, TransactionType.CREDIT, account2);
			Transaction transaction2_2 = new Transaction(-1000, "Withdrawal", tomorrow, TransactionType.DEBIT, account2);
			Transaction transaction2_3 = new Transaction(2000, "Deposit", now, TransactionType.CREDIT, account2);

			account1.addTransaction(transaction1_1);
			account1.addTransaction(transaction1_2);
			account1.addTransaction(transaction1_3);

			account2.addTransaction(transaction2_1);
			account2.addTransaction(transaction2_2);
			account2.addTransaction(transaction2_3);

			client1.addAccount(account1);
			client1.addAccount(account2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			transactionRepository.save(transaction1_1);
			transactionRepository.save(transaction1_2);
			transactionRepository.save(transaction1_3);
			transactionRepository.save(transaction2_1);
			transactionRepository.save(transaction2_2);
			transactionRepository.save(transaction2_3);

			// Create Accounts and Transactions for client2
			Account account3 = new Account("VIN003", 2000, now, client2, new HashSet<>());
			Account account4 = new Account("VIN004", 3000, tomorrow, client2, new HashSet<>());

			Transaction transaction3_1 = new Transaction(500, "Deposit", now, TransactionType.CREDIT, account3);
			Transaction transaction3_2 = new Transaction(-200, "Withdrawal", tomorrow, TransactionType.DEBIT, account3);
			Transaction transaction3_3 = new Transaction(1300, "Deposit", now, TransactionType.CREDIT, account3);

			Transaction transaction4_1 = new Transaction(1500, "Deposit", now, TransactionType.CREDIT, account4);
			Transaction transaction4_2 = new Transaction(-800, "Withdrawal", tomorrow, TransactionType.DEBIT, account4);
			Transaction transaction4_3 = new Transaction(700, "Deposit", now, TransactionType.CREDIT, account4);

			account3.addTransaction(transaction3_1);
			account3.addTransaction(transaction3_2);
			account3.addTransaction(transaction3_3);

			account4.addTransaction(transaction4_1);
			account4.addTransaction(transaction4_2);
			account4.addTransaction(transaction4_3);

			client2.addAccount(account3);
			client2.addAccount(account4);

			accountRepository.save(account3);
			accountRepository.save(account4);
			transactionRepository.save(transaction3_1);
			transactionRepository.save(transaction3_2);
			transactionRepository.save(transaction3_3);
			transactionRepository.save(transaction4_1);
			transactionRepository.save(transaction4_2);
			transactionRepository.save(transaction4_3);

			// Create Accounts and Transactions for client3
			Account account5 = new Account("VIN005", 1000, now, client3, new HashSet<>());
			Account account6 = new Account("VIN006", 4000, tomorrow, client3, new HashSet<>());

			Transaction transaction5_1 = new Transaction(300, "Deposit", now, TransactionType.CREDIT, account5);
			Transaction transaction5_2 = new Transaction(-100, "Withdrawal", tomorrow, TransactionType.DEBIT, account5);
			Transaction transaction5_3 = new Transaction(600, "Deposit", now, TransactionType.CREDIT, account5);

			Transaction transaction6_1 = new Transaction(2500, "Deposit", now, TransactionType.CREDIT, account6);
			Transaction transaction6_2 = new Transaction(-1500, "Withdrawal", tomorrow, TransactionType.DEBIT, account6);
			Transaction transaction6_3 = new Transaction(500, "Deposit", now, TransactionType.CREDIT, account6);

			account5.addTransaction(transaction5_1);
			account5.addTransaction(transaction5_2);
			account5.addTransaction(transaction5_3);

			account6.addTransaction(transaction6_1);
			account6.addTransaction(transaction6_2);
			account6.addTransaction(transaction6_3);

			client3.addAccount(account5);
			client3.addAccount(account6);

			accountRepository.save(account5);
			accountRepository.save(account6);
			transactionRepository.save(transaction5_1);
			transactionRepository.save(transaction5_2);
			transactionRepository.save(transaction5_3);
			transactionRepository.save(transaction6_1);
			transactionRepository.save(transaction6_2);
			transactionRepository.save(transaction6_3);

			// Create Loans
			Loan mortgage = new Loan("mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60), new HashSet<>());
			Loan personal = new Loan("personal", 100000, Arrays.asList(6, 12, 24), new HashSet<>());
			Loan automotive = new Loan("automotive", 300000, Arrays.asList(6, 12, 24, 36), new HashSet<>());

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			// Create ClientLoans
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

			// Save ClientLoans
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);


			Card card1 = new Card(CardType.DEBIT,CardColor.GOLD,now,now.plusYears(5));
			Card card2 = new Card(CardType.DEBIT,CardColor.TITANIUM,now,now.plusYears(5));
			client1.addCards(card1);
			client1.addCards(card2);
			cardRepository.save(card1);
			cardRepository.save(card2);

			Card card3 = new Card(CardType.DEBIT,CardColor.SILVER,now,now.plusYears(5));
			client2.addCards(card3);
			cardRepository.save(card3);
		};


	}


	}





