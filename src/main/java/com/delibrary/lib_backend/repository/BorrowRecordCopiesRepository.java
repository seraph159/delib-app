package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.BorrowRecordCopies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordCopiesRepository extends JpaRepository<BorrowRecordCopies, Long> {
    List<BorrowRecordCopies> findByClientEmail(String email);
}