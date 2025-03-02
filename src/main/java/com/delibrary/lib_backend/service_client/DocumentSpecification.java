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

    private final DocumentSearchDto searchDTO;

    public DocumentSpecification(DocumentSearchDto searchDTO) {
        this.searchDTO = searchDTO;
    }

    @Override
    public Predicate toPredicate(Root<Document> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Perform LEFT JOINs
        Join<Document, Book> bookJoin = root.join("book", JoinType.LEFT);
        Join<Document, Magazine> magazineJoin = root.join("magazine", JoinType.LEFT);
        Join<Document, JournalArticle> journalArticleJoin = root.join("journalArticle", JoinType.LEFT);

        // Search by title
        if (searchDTO.getTitle() != null && !searchDTO.getTitle().isEmpty()) {
            Predicate bookTitle = cb.like(bookJoin.get("title"), "%" + searchDTO.getTitle() + "%");
            Predicate magazineName = cb.like(magazineJoin.get("name"), "%" + searchDTO.getTitle() + "%");
            Predicate articleTitle = cb.like(journalArticleJoin.get("title"), "%" + searchDTO.getTitle() + "%");
            predicates.add(cb.or(bookTitle, magazineName, articleTitle));
        }

        // Search by ISBN
        if (searchDTO.getIsbn() != null && !searchDTO.getIsbn().isEmpty()) {
            Predicate bookIsbn = cb.equal(bookJoin.get("isbn"), searchDTO.getIsbn());
            Predicate magazineIsbn = cb.equal(magazineJoin.get("isbn"), searchDTO.getIsbn());
            predicates.add(cb.or(bookIsbn, magazineIsbn));
        }

        // Search by year
        if (searchDTO.getYear() != null) {
            Predicate bookYear = cb.equal(bookJoin.get("year"), searchDTO.getYear());
            Predicate magazineYear = cb.equal(magazineJoin.get("year"), searchDTO.getYear());
            Predicate articleYear = cb.equal(journalArticleJoin.get("year"), searchDTO.getYear());
            predicates.add(cb.or(bookYear, magazineYear, articleYear));
        }

        // Search by publisher
        if (searchDTO.getPublisher() != null && !searchDTO.getPublisher().isEmpty()) {
            Predicate bookPublisher = cb.like(bookJoin.get("publisher"), "%" + searchDTO.getPublisher() + "%");
            Predicate magazinePublisher = cb.like(magazineJoin.get("publisher"), "%" + searchDTO.getPublisher() + "%");
            Predicate articlePublisher = cb.like(journalArticleJoin.get("publisher"), "%" + searchDTO.getPublisher() + "%");
            predicates.add(cb.or(bookPublisher, magazinePublisher, articlePublisher));
        }

        // Filter available documents
        if (searchDTO.isOnlyAvailable()) {
            predicates.add(cb.greaterThan(root.get("availableCopies"), 0));
        }

        // Apply sorting
        if (searchDTO.getSortBy() != null && !searchDTO.getSortBy().isEmpty()) {
            // Determine sort direction (default to "asc" if invalid or null)
            boolean isAscending = "asc".equalsIgnoreCase(searchDTO.getSortDirection()) ||
                    searchDTO.getSortDirection() == null ||
                    !searchDTO.getSortDirection().equalsIgnoreCase("desc");

            String sortBy = searchDTO.getSortBy().toLowerCase();
            switch (sortBy) {
                case "publisher":
                    Expression<String> publisherExpr = cb.coalesce(
                            cb.coalesce(bookJoin.get("publisher"), magazineJoin.get("publisher")),
                            journalArticleJoin.get("publisher")
                    );
                    query.orderBy(isAscending ? cb.asc(publisherExpr) : cb.desc(publisherExpr));
                    break;
                case "title":
                    Expression<String> titleExpr = cb.coalesce(
                            cb.coalesce(bookJoin.get("title"), magazineJoin.get("name")),
                            journalArticleJoin.get("title")
                    );
                    query.orderBy(isAscending ? cb.asc(titleExpr) : cb.desc(titleExpr));
                    break;
                case "year":
                    Expression<Integer> yearExpr = cb.coalesce(
                            cb.coalesce(bookJoin.get("year"), magazineJoin.get("year")),
                            journalArticleJoin.get("year")
                    );
                    query.orderBy(isAscending ? cb.asc(yearExpr) : cb.desc(yearExpr));
                    break;
                case "isbn":
                    Expression<String> isbnExpr = cb.coalesce(
                            bookJoin.get("isbn"),
                            magazineJoin.get("isbn")
                    );
                    query.orderBy(isAscending ? cb.asc(isbnExpr) : cb.desc(isbnExpr));
                    break;
                case "availablecopies":
                    query.orderBy(isAscending ? cb.asc(root.get("availableCopies")) : cb.desc(root.get("availableCopies")));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sortBy field: " + sortBy);
            }
        }

        // Combine predicates with AND
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}