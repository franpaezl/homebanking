package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.ClientLoan;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    Client getAuthenticatedClient(Authentication authentication);

    List<Client> getAllClient();

    List<ClientDTO> getAllClientDTO();

    Client getClientById(Long id);

    ClientDTO getClientDTO(Client client);

    String encodedPassword(RegisterDTO registerDTO);

    Client createNewClient(RegisterDTO registerDTO);

    void saveClient(Client client);

    void addAccountToClient(Client client,Account account);

    void addCardsToClient(Client client, Card card);

    void addClientLoanToClient(Client client, ClientLoan clientLoan);

}