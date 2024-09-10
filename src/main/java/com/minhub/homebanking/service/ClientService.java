package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.RegisterDTO;
import com.minhub.homebanking.models.Client;
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

}