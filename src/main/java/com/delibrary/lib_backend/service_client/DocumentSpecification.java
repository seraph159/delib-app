package com.delibrary.lib_backend.service_client;

import com.delibrary.lib_backend.dto.DocumentSearchDto;
import com.delibrary.lib_backend.entity.Book;
import com.delibrary.lib_backend.entity.Document;
import com.delibrary.lib_backend.entity.JournalArticle;
import com.delibrary.lib_backend.entity.Magazine;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentSpecification implements Specification<Document> {

    private DocumentSearchDto searchDTO;

    public DocumentSpecification(DocumentSearchDto searchDTO) {
        this.searchDTO = searchDTO;
    }

    @Override
    public Predicate toPredicate(Root<Document> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        // List to store individual predicates
        List<Predicate> predicates = new ArrayList<>();

        // Perform LEFT JOINs
        Join<Document, Book> bookJoin = root.join("book", JoinType.LEFT);
        Join<Document, Magazine> magazineJoin = root.join("magazine", JoinType.LEFT);
        Join<Document, JournalArticle> journalArticleJoin = root.join("journalArticle", JoinType.LEFT);

        // Search by title
        if (searchDTO.getTitle() != null) {
            Predicate bookTitle = criteriaBuilder.like(bookJoin.get("title"), "%" + searchDTO.getTitle() + "%");
            Predicate magazineName = criteriaBuilder.like(magazineJoin.get("name"), "%" + searchDTO.getTitle() + "%");
            Predicate articleTitle = criteriaBuilder.like(journalArticleJoin.get("title"), "%" + searchDTO.getTitle() + "%");

            predicates.add(criteriaBuilder.or(bookTitle, magazineName, articleTitle));
        }

        // Search by ISBN
        if (searchDTO.getIsbn() != null) {
            Predicate bookIsbn = criteriaBuilder.equal(bookJoin.get("isbn"), searchDTO.getIsbn());
            Predicate magazineIsbn = criteriaBuilder.equal(magazineJoin.get("isbn"), searchDTO.getIsbn());

            predicates.add(criteriaBuilder.or(bookIsbn, magazineIsbn));
        }

        // Search by year
        if (searchDTO.getYear() != null) {
            Predicate bookYear = criteriaBuilder.equal(bookJoin.get("year"), searchDTO.getYear());
            Predicate magazineYear = criteriaBuilder.equal(magazineJoin.get("year"), searchDTO.getYear());
            Predicate articleYear = criteriaBuilder.equal(journalArticleJoin.get("year"), searchDTO.getYear());

            predicates.add(criteriaBuilder.or(bookYear, magazineYear, articleYear));
        }

        // Search by publisher
        if (searchDTO.getPublisher() != null) {
            Predicate bookPublisher = criteriaBuilder.like(bookJoin.get("publisher"), "%" + searchDTO.getPublisher() + "%");
            Predicate magazinePublisher = criteriaBuilder.like(magazineJoin.get("publisher"), "%" + searchDTO.getPublisher() + "%");
            Predicate articlePublisher = criteriaBuilder.like(journalArticleJoin.get("publisher"), "%" + searchDTO.getPublisher() + "%");

            predicates.add(criteriaBuilder.or(bookPublisher, magazinePublisher, articlePublisher));
        }

        // Filter available documents
        if (searchDTO.isOnlyAvailable()) {
            predicates.add(criteriaBuilder.greaterThan(root.get("availableCopies"), 0));
        }

        // Combine all predicates with AND operator
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}