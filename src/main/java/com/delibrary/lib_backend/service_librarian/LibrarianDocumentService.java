package com.delibrary.lib_backend.service_librarian;

import com.delibrary.lib_backend.dto.BookDto;
import com.delibrary.lib_backend.dto.DocumentResponseDto;
import com.delibrary.lib_backend.dto.JournalDto;
import com.delibrary.lib_backend.dto.MagazineDto;
import com.delibrary.lib_backend.entity.Book;
import com.delibrary.lib_backend.entity.Document;
import com.delibrary.lib_backend.entity.JournalArticle;
import com.delibrary.lib_backend.entity.Magazine;
import com.delibrary.lib_backend.repository.BookRepository;
import com.delibrary.lib_backend.repository.DocumentRepository;
import com.delibrary.lib_backend.repository.JournalArticleRepository;
import com.delibrary.lib_backend.repository.MagazineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibrarianDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private JournalArticleRepository journalArticleRepository;

    public void addBook(BookDto bookDto) {
        String documentId = generateDocumentId(bookDto.getIsbn());

        // Check if the Document entity already exists
        Optional<Document> existingDocumentOpt = documentRepository.findById(documentId);
        if (!existingDocumentOpt.isPresent()) {

            Book book = new Book();
            book.setDocumentId(documentId);
            book.setTitle(bookDto.getTitle());
            book.setDocumentId(documentId);
            book.setPublisher(bookDto.getPublisher());
            book.setIsbn(bookDto.getIsbn());
            book.setEdition(bookDto.getEdition());
            book.setYear(bookDto.getYear());
            book.setImageUrl(bookDto.getImageUrl());
            bookRepository.save(book);

            Document document = new Document();
            document.setDocumentId(documentId);
            document.setElectronic(bookDto.isElectronic());
            document.setAvailableCopies(bookDto.getAvailableCopies());
            document.setNoOfBorrowers(bookDto.getNoOfBorrowers());

            document.setBook(book);
            document.setMagazine(null);  // Explicitly set to null
            document.setJournalArticle(null);  // Explicitly set to null
            documentRepository.save(document);

        }

    }


    public void addMagazine(MagazineDto magazineDto) {
        String documentId = generateDocumentId(magazineDto.getIsbn());

        // Check if the Document entity already exists
        Optional<Document> existingDocumentOpt = documentRepository.findById(documentId);
        if (!existingDocumentOpt.isPresent()) {

        Magazine magazine = new Magazine();
        magazine.setDocumentId(documentId);
        magazine.setName(magazineDto.getTitle());
        magazine.setPublisher(magazineDto.getPublisher());
        magazine.setIsbn(magazineDto.getIsbn());
        magazine.setYear(magazineDto.getYear());
        magazine.setMonth(magazineDto.getMonth());
        magazine.setImageUrl(magazineDto.getImageUrl());
        magazineRepository.save(magazine);

        // Create a new Document entity
        Document document = new Document();
        document.setDocumentId(documentId);
        document.setElectronic(magazineDto.isElectronic());
        document.setAvailableCopies(magazineDto.getAvailableCopies());
        document.setNoOfBorrowers(magazineDto.getNoOfBorrowers());

        document.setBook(null); // Explicitly set to null
        document.setMagazine(magazine);
        document.setJournalArticle(null);  // Explicitly set to null
        documentRepository.save(document);

        }
    }

    public void addJournal(JournalDto journalDto) {
        String documentId = generateDocumentId(journalDto.getTitle());

        // Check if the Document entity already exists
        Optional<Document> existingDocumentOpt = documentRepository.findById(documentId);
        if (!existingDocumentOpt.isPresent()) {

        JournalArticle journal = new JournalArticle();
        journal.setDocumentId(documentId);
        journal.setName(journalDto.getTitle());
        journal.setTitle(journalDto.getTitle());
        journal.setJournal(journalDto.getJournal());
        journal.setNumber(journalDto.getNumber());
        journal.setPublisher(journalDto.getPublisher());
        journal.setYear(journalDto.getYear());
        journal.setIssue(journalDto.getIssue());
        journal.setImageUrl(journalDto.getImageUrl());
        journalArticleRepository.save(journal);

        Document document = new Document();
        document.setDocumentId(documentId);
        document.setElectronic(journalDto.isElectronic());
        document.setAvailableCopies(journalDto.getAvailableCopies());
        document.setNoOfBorrowers(journalDto.getNoOfBorrowers());

        document.setBook(null); // Explicitly set to null
        document.setMagazine(null); // Explicitly set to null
        document.setJournalArticle(journal);
        documentRepository.save(document);
        }
    }

    // returns a string of 10 digit numbers unique and different for each input
    private String generateDocumentId(String prefix) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(prefix.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(10);
            while (hashtext.length() < 10) {
                hashtext = "0" + hashtext;
            }

            return hashtext.substring(0, 10);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate document ID", e);
        }
    }


    // Fetch documents with availableCopies
    public List<DocumentResponseDto> findDocumentsByType(String type) {
        switch (type.toLowerCase()) {
            case "book":
                List<Book> books = bookRepository.findAll();
                return books.stream().map(book -> {
                    Document doc = documentRepository.findById(book.getDocumentId())
                            .orElseThrow(() -> new EntityNotFoundException("Document not found for book: " + book.getDocumentId()));
                    return new DocumentResponseDto(
                            book.getCopyId(),
                            book.getDocumentId(),
                            book.getTitle(),
                            null,
                            book.getPublisher(),
                            book.getIsbn(),
                            book.getEdition(),
                            book.getYear(),
                            0,
                            null,
                            0,
                            0,
                            doc.getAvailableCopies(),
                            book.getImageUrl()
                    );
                }).collect(Collectors.toList());
            case "magazine":
                List<Magazine> magazines = magazineRepository.findAll();
                return magazines.stream().map(magazine -> {
                    Document doc = documentRepository.findById(magazine.getDocumentId())
                            .orElseThrow(() -> new EntityNotFoundException("Document not found for magazine: " + magazine.getDocumentId()));
                    return new DocumentResponseDto(
                            magazine.getCopyId(),
                            magazine.getDocumentId(),
                            magazine.getName(), // title
                            magazine.getName(), // name
                            magazine.getPublisher(),
                            magazine.getIsbn(),
                            0,
                            magazine.getYear(),
                            magazine.getMonth(),
                            null,
                            0,
                            0,
                            doc.getAvailableCopies(),
                            magazine.getImageUrl()
                    );
                }).collect(Collectors.toList());
            case "journal":
                List<JournalArticle> journals = journalArticleRepository.findAll();
                return journals.stream().map(journal -> {
                    Document doc = documentRepository.findById(journal.getDocumentId())
                            .orElseThrow(() -> new EntityNotFoundException("Document not found for journal: " + journal.getDocumentId()));
                    return new DocumentResponseDto(
                            journal.getCopyId(),
                            journal.getDocumentId(),
                            journal.getTitle(),
                            null,
                            journal.getPublisher(),
                            null,
                            0,
                            journal.getYear(),
                            0,
                            journal.getJournal(),
                            journal.getNumber(),
                            journal.getIssue(),
                            doc.getAvailableCopies(),
                            journal.getImageUrl()
                    );
                }).collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Invalid document type");
        }
    }

    // -----------------------------------------------------------------------------

    // Update document including availableCopies
    public Object updateDocument(String type, String id, Map<String, Object> documentData) {
        switch (type.toLowerCase()) {
            case "book":
                Book book = updateBook(id, documentData);
                return book;
            case "magazine":
                Magazine magazine = updateMagazine(id, documentData);
                return magazine;
            case "journal":
                JournalArticle journal = updateJournal(id, documentData);
                return journal;
            default:
                throw new IllegalArgumentException("Invalid document type");
        }
    }

    public void updateAvailableCopies(String id, int newCopies) {
        // Update availableCopies in Document entity
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));
        if (newCopies < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }

        document.setAvailableCopies(newCopies);
        documentRepository.save(document);
    }

    private Book updateBook(String id, Map<String, Object> documentData) {
        Book book = bookRepository.findByDocumentId(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        book.setTitle((String) documentData.get("title"));
        book.setPublisher((String) documentData.get("publisher"));
        book.setIsbn((String) documentData.get("isbn"));
        book.setEdition((Integer) documentData.get("edition"));
        book.setYear((Integer) documentData.get("year"));

        return bookRepository.save(book);
    }

    private Magazine updateMagazine(String id, Map<String, Object> documentData) {
        Magazine magazine = magazineRepository.findByDocumentId(id)
                .orElseThrow(() -> new EntityNotFoundException("Magazine not found"));

        magazine.setName((String) documentData.get("name"));
        magazine.setPublisher((String) documentData.get("publisher"));
        magazine.setIsbn((String) documentData.get("isbn"));
        magazine.setYear((Integer) documentData.get("year"));
        magazine.setMonth((Integer) documentData.get("month"));

        return magazineRepository.save(magazine);
    }

    private JournalArticle updateJournal(String id, Map<String, Object> documentData) {
        JournalArticle journal = journalArticleRepository.findByDocumentId(id)
                .orElseThrow(() -> new EntityNotFoundException("Journal not found"));

        journal.setName((String) documentData.get("title"));
        journal.setTitle((String) documentData.get("title"));
        journal.setJournal((String) documentData.get("journal"));
        journal.setNumber((Integer) documentData.get("number"));
        journal.setYear((Integer) documentData.get("year"));
        journal.setIssue((Integer) documentData.get("issue"));
        journal.setPublisher((String) documentData.get("publisher"));

        return journalArticleRepository.save(journal);
    }

    // -----------------------------------------------------------------------------
    public void deleteDocument(String type, String id) {
        switch (type.toLowerCase()) {
            case "book":
                deleteBook(id);
                break;
            case "magazine":
                deleteMagazine(id);
                break;
            case "journal":
                deleteJournal(id);
                break;
            default:
                throw new IllegalArgumentException("Invalid document type");
        }
    }

    private void deleteBook(String documentId) {
        // First, find and delete the associated Document entity
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with documentId: " + documentId));
        documentRepository.delete(document);

        // Then, delete the Book entity (if it still exists independently, though it might be cascade-deleted)
        Book book = bookRepository.findByDocumentId(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with documentId: " + documentId));
        bookRepository.delete(book);
    }

    private void deleteMagazine(String documentId) {
        // First, find and delete the associated Document entity
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with documentId: " + documentId));
        documentRepository.delete(document);

        // Then, delete the Magazine entity
        Magazine magazine = magazineRepository.findByDocumentId(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Magazine not found with documentId: " + documentId));
        magazineRepository.delete(magazine);
    }

    private void deleteJournal(String documentId) {
        // First, find and delete the associated Document entity
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with documentId: " + documentId));
        documentRepository.delete(document);

        // Then, delete the JournalArticle entity
        JournalArticle journalArticle = journalArticleRepository.findByDocumentId(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Journal not found with documentId: " + documentId));
        journalArticleRepository.delete(journalArticle);
    }

    // -----------------------------------------------------------------------------

}
