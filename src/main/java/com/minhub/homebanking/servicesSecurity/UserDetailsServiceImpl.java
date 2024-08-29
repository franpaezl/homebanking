package com.minhub.homebanking.servicesSecurity;

import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username);

        if (client == null) {
            throw new UsernameNotFoundException(username);
        }


        String role = username.contains("@admin") ? "ADMIN" : "CLIENT";

        return User.withUsername(username)
                .password(client.getPassword())
                .roles(role)
                .build();
    }
}
