package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAddressRepository extends JpaRepository<ClientAddress, String> {

}
