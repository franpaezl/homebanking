package com.minhub.homebanking;

import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository) {
		return (args) -> {

			clientRepository.save(new Client("Francisco", "Páez Lastra" ,"francsicopaezlastra@gmail.com"));
			clientRepository.save(new Client("Jose", "Perez","joseperez@hotmail.com"));
		};
	}
}




