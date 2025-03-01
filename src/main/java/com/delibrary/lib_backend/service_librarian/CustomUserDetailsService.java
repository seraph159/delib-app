package com.delibrary.lib_backend.service_librarian;

import com.delibrary.lib_backend.entity.Client;
import com.delibrary.lib_backend.entity.User;
import com.delibrary.lib_backend.repository.ClientRepository;
import com.delibrary.lib_backend.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = findByEmail(email);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private Optional<User> findByEmail(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            return Optional.of(client.get());
        }
        return librarianRepository.findByEmail(email).map(librarian -> (User) librarian);
    }
}