package com.minhub.homebanking;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashSet;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			LocalDateTime dateTimeNow = LocalDateTime.now();

			// Crear Clientes
			Client client1 = new Client("Melba", "Morel", "melbamorel@hotmail.com", new HashSet<>());
			Client client2 = new Client("Fran", "Paez", "fran.paez@example.com", new HashSet<>());
			Client client3 = new Client("Pepe", "Pérez", "pepe.perez@example.com", new HashSet<>());

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			// Crear Cuentas y Transacciones para client1
			Account account1 = new Account("VIN001", 5000, dateTimeNow, client1, new HashSet<>());
			Account account2 = new Account("VIN002", 7500, dateTimeNow, client1, new HashSet<>());

			Transaction transaction1_1 = new Transaction(1000, dateTimeNow, TransactionType.ADD, "Deposit", account1);
			Transaction transaction1_2 = new Transaction(-500, dateTimeNow, TransactionType.SUBTRACT, "Withdrawal", account1);
			Transaction transaction1_3 = new Transaction(1500, dateTimeNow, TransactionType.ADD, "Deposit", account1);

			Transaction transaction2_1 = new Transaction(2000, dateTimeNow, TransactionType.ADD, "Deposit", account2);
			Transaction transaction2_2 = new Transaction(-1000, dateTimeNow, TransactionType.SUBTRACT, "Withdrawal", account2);
			Transaction transaction2_3 = new Transaction(2000, dateTimeNow, TransactionType.ADD, "Deposit", account2);

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

			// Crear Cuentas y Transacciones para client2
			Account account3 = new Account("VIN003", 2000, dateTimeNow, client2, new HashSet<>());
			Account account4 = new Account("VIN004", 3000, dateTimeNow, client2, new HashSet<>());

			Transaction transaction3_1 = new Transaction(500, dateTimeNow, TransactionType.ADD, "Deposit", account3);
			Transaction transaction3_2 = new Transaction(-200, dateTimeNow, TransactionType.SUBTRACT, "Withdrawal", account3);
			Transaction transaction3_3 = new Transaction(1300, dateTimeNow, TransactionType.ADD, "Deposit", account3);

			Transaction transaction4_1 = new Transaction(1500, dateTimeNow, TransactionType.ADD, "Deposit", account4);
			Transaction transaction4_2 = new Transaction(-800, dateTimeNow, TransactionType.SUBTRACT, "Withdrawal", account4);
			Transaction transaction4_3 = new Transaction(700, dateTimeNow, TransactionType.ADD, "Deposit", account4);

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

			// Crear Cuentas y Transacciones para client3
			Account account5 = new Account("VIN005", 1000, dateTimeNow, client3, new HashSet<>());
			Account account6 = new Account("VIN006", 4000, dateTimeNow, client3, new HashSet<>());

			Transaction transaction5_1 = new Transaction(300, dateTimeNow, TransactionType.ADD, "Deposit", account5);
			Transaction transaction5_2 = new Transaction(-100, dateTimeNow, TransactionType.SUBTRACT, "Withdrawal", account5);
			Transaction transaction5_3 = new Transaction(600, dateTimeNow, TransactionType.ADD, "Deposit", account5);

			Transaction transaction6_1 = new Transaction(2500, dateTimeNow, TransactionType.ADD, "Deposit", account6);
			Transaction transaction6_2 = new Transaction(-1500, dateTimeNow, TransactionType.SUBTRACT, "Withdrawal", account6);
			Transaction transaction6_3 = new Transaction(500, dateTimeNow, TransactionType.ADD, "Deposit", account6);

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

			// Guardar Clientes con sus Cuentas
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
		};
	}
}
