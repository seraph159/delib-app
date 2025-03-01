package com.delibrary.lib_backend.service_librarian;

import com.delibrary.lib_backend.dto.ClientRegistrationDto;
import com.delibrary.lib_backend.entity.Client;
import com.delibrary.lib_backend.entity.ClientAddress;
import com.delibrary.lib_backend.repository.ClientAddressRepository;
import com.delibrary.lib_backend.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientAddressRepository clientAddressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerClient(ClientRegistrationDto registrationDto) {

        if (clientRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create Client
        Client client = new Client();
        client.setEmail(registrationDto.getEmail());
        client.setName(registrationDto.getName());
        client.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        client.setRole("CLIENT");

        // Create ClientAddress
        ClientAddress clientAddress = new ClientAddress();
        clientAddress.setEmail(registrationDto.getEmail());
        clientAddress.setAddress(registrationDto.getAddress());
        clientAddress.setCity(registrationDto.getCity());
        clientAddress.setState(registrationDto.getState());
        clientAddress.setZipcode(registrationDto.getZipcode());

        // Set bidirectional relationship
        client.setClientAddress(clientAddress);
        clientAddress.setClient(client);

        // Save (cascading will save ClientAddress)
        clientRepository.save(client);
    }

    public List<ClientRegistrationDto> getAllClientsWithAddress() {
        return clientRepository.findAllClientsWithAddress();
    }

    public void updateClient(String emailAddress, ClientRegistrationDto updateDto) {
        Client client = clientRepository.findByEmail(emailAddress)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with email: " + emailAddress));

        client.setName(updateDto.getName());
        clientRepository.save(client);

        ClientAddress clientAddress = clientAddressRepository.findById(emailAddress)
                .orElseThrow(() -> new EntityNotFoundException("Client address not found for email: " + emailAddress));

        clientAddress.setAddress(updateDto.getAddress());
        clientAddress.setCity(updateDto.getCity());
        clientAddress.setState(updateDto.getState());
        clientAddress.setZipcode(updateDto.getZipcode());
        clientAddressRepository.save(clientAddress);
    }

    public void deleteClient(String emailAddress) {

        // Check if the client exists
        if (!clientRepository.existsByEmail(emailAddress)) {
            throw new EntityNotFoundException("Client not found with email: " + emailAddress);
        }

        // Delete the client
        clientRepository.deleteById(emailAddress);
    }


}