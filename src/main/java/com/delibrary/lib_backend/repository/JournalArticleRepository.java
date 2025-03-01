package com.delibrary.lib_backend.repository;

import com.delibrary.lib_backend.entity.JournalArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalArticleRepository extends JpaRepository<JournalArticle, Long> {

    Optional<JournalArticle> findByDocumentId(String documentId);

}