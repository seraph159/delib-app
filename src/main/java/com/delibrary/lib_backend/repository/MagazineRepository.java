package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Optional<Magazine> findByDocumentId(String documentId);
}
