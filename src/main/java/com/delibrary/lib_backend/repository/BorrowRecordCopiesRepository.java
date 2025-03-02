package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.BorrowRecordCopies;
import com.delibrary.lib_backend.entity.Client;
import com.delibrary.lib_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordCopiesRepository extends JpaRepository<BorrowRecordCopies, Long> {
    List<BorrowRecordCopies> findByClientEmail(String email);
    Optional<BorrowRecordCopies> findByClientAndDocumentAndReturnDateIsNull(Client client, Document document);
    List<BorrowRecordCopies> findByClientAndReturnDateIsNull(Client client);
    List<BorrowRecordCopies> findAllByClientAndDocumentAndReturnDateIsNull(Client client, Document document);
}