package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.dto.ClientRegistrationDto;
import com.delibrary.lib_backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findByCreditCardCardNo(String cardNo);

    boolean existsByEmail(String email);

    @Query("SELECT new com.delibrary.lib_backend.dto.ClientRegistrationDto(c.email, c.name, ca.address, ca.city, ca.state, ca.zipcode) " +
            "FROM Client c LEFT JOIN ClientAddress ca ON c.email = ca.email")
    List<ClientRegistrationDto> findAllClientsWithAddress();
}