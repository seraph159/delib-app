package com.delibrary.lib_backend.service_librarian;

import com.delibrary.lib_backend.entity.Client;
import com.delibrary.lib_backend.entity.ClientAddress;
import com.delibrary.lib_backend.entity.Librarian;
import com.delibrary.lib_backend.entity.User;
import com.delibrary.lib_backend.repository.ClientRepository;
import com.delibrary.lib_backend.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (user instanceof Client) {
            return registerClient((Client) user);
        } else if (user instanceof Librarian) {
            return registerLibrarian((Librarian) user);
        }
        throw new IllegalArgumentException("Invalid user type");
    }

    private Client registerClient(Client client) {

        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        ClientAddress clientAddress = new ClientAddress();
        clientAddress.setEmail(client.getEmail());

        client.setEmail(client.getEmail());
        client.setName(client.getName());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setRole("CLIENT");
        client.setClientAddress(clientAddress);
        return clientRepository.save(client);
    }

    private Librarian registerLibrarian(Librarian librarian) {

        if (librarianRepository.existsByEmail(librarian.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        librarian.setSsn(librarian.getSsn());
        librarian.setEmail(librarian.getEmail());
        librarian.setName(librarian.getName());
        librarian.setSalary(librarian.getSalary());
        librarian.setPassword(passwordEncoder.encode(librarian.getPassword()));
        librarian.setRole("LIBRARIAN");
        return librarianRepository.save(librarian);
    }

}