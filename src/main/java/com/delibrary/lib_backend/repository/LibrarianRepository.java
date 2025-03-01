package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

    Optional<Librarian> findByEmail(String email);
    boolean existsByEmail(String email);
}