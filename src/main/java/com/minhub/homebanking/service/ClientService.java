package com.minhub.homebanking.service;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClient();
    List<ClientDTO> getAllClientDTO();
    Client getClientById(Long id);
    ClientDTO getClientDTO(Client client);
}
