package com.minhub.homebanking;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AcountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AcountRepository acountRepository) {
		return (args) -> {
			LocalDate dateNow = LocalDate.now();
			LocalDate dateTomorrow = dateNow.plusDays(1);


			Client client1 = new Client("Melba", "Morel", "melbamorel@hotmail.com", new HashSet<>());
			Account account = new Account("VIN001", 5000, dateNow, client1);
			Account account1 = new Account("VIN002", 7500, dateTomorrow, client1);
			client1.addAccount(account);
			client1.addAccount(account1);


			Client client2 = new Client("Francisco", "Paez", "francisco.paez@example.com", new HashSet<>());
			Account account3 = new Account("VIN003", 6000, dateNow, client2);
			Account account4 = new Account("VIN004", 8000, dateTomorrow, client2);
			client2.addAccount(account3);
			client2.addAccount(account4);


			Client client3 = new Client("Pepe", "Pérez", "pepe.perez@example.com", new HashSet<>());
			Account account5= new Account("VIN005", 7000, dateNow, client3);
			Account account6 = new Account("VIN006", 9000, dateTomorrow, client3);
			client3.addAccount(account5);
			client3.addAccount(account6);

			acountRepository.save(account);
			acountRepository.save(account1);
			acountRepository.save(account6);
			acountRepository.save(account3);
			acountRepository.save(account4);
			acountRepository.save(account5);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
		};
	}
}





