package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//public interface DocumentRepository extends JpaRepository<Document, String> {
//
//}

public interface DocumentRepository extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {
}