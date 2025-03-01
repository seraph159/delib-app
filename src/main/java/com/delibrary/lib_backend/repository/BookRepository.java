package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByDocumentId(String documentId);

}
