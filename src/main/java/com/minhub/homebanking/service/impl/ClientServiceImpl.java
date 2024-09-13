package com.minhub.homebanking.service.impl;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.ClientLoan;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }

    @Override
    public Client getAuthenticatedClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return getAllClient().stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClientDTO(Client client) {
        return new ClientDTO(client);
    }

    @Override
    public String encodedPassword(RegisterDTO registerDTO) {
        return passwordEncoder.encode(registerDTO.password());
    }

    @Override
    public Client createNewClient(RegisterDTO registerDTO) {
        // Codifica la contraseña del cliente
        String encodedPassword = encodedPassword(registerDTO);

        // Crea un nuevo objeto Client con los datos proporcionados
        return new Client(
                registerDTO.firstName(),
                registerDTO.lastName(),
                registerDTO.email(),
                encodedPassword
        );
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void addAccountToClient(Client client, Account account) {
        client.addAccount(account);
    }

    @Override
    public void addCardsToClient(Client client, Card card) {
        client.addCards(card);
    }

    @Override
    public void addClientLoanToClient(Client client, ClientLoan clientLoan) {
        client.addClientLoans(clientLoan);
    }
}



