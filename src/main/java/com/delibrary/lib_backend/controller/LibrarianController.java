package com.delibrary.lib_backend.controller;

import com.delibrary.lib_backend.client.imagestorage.ImageStorageClient;
import com.delibrary.lib_backend.dto.*;
import com.delibrary.lib_backend.service_librarian.ClientService;
import com.delibrary.lib_backend.service_librarian.LibrarianDocumentService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/librarian")
@PreAuthorize("hasRole('LIBRARIAN')")
public class LibrarianController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private LibrarianDocumentService documentService;

    private static final Logger logger = LoggerFactory.getLogger(LibrarianController.class);

    @Autowired
    private ImageStorageClient imageStorageClient;

    // f-endpoint for registering a client's address
    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegistrationDto registrationDto) {
        try {
            clientService.registerClient(registrationDto);
            return ResponseEntity.ok("Client Registered Successfully!");
        } catch (Exception e) {
            logger.error("Failed to register client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register client: " + e.getMessage());
        }
    }

    // f-endpoint for fetching all clients
    @GetMapping("/clients")
    public ResponseEntity<?> getAllClients() {
        try {
            List<ClientRegistrationDto> clients = clientService.getAllClientsWithAddress();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            logger.error("An error occurred while fetching clients: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching clients: " + e.getMessage());
        }
    }

    // f-endpoint to update a client
    @PutMapping("/clients/{emailAddress}")
    public ResponseEntity<?> updateClient(@PathVariable String emailAddress, @RequestBody ClientRegistrationDto updateDto) {
        try {
            clientService.updateClient(emailAddress, updateDto);
            return ResponseEntity.ok().body("Client updated successfully");
        } catch (EntityNotFoundException e) {
            logger.warn("Client to update not found: {}", emailAddress);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while updating the client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the client: " + e.getMessage());
        }
    }

    // f-endpoint to delete a client
    @DeleteMapping("/clients/{emailAddress}")
    public ResponseEntity<?> deleteClient(@PathVariable String emailAddress) {
        try {
            clientService.deleteClient(emailAddress);
            return ResponseEntity.ok().body("Client deleted successfully");
        } catch (EntityNotFoundException e) {
            logger.warn("Client to delete not found: {}", emailAddress);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while deleting the client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the client: " + e.getMessage());
        }
    }

    // --------------------------

    // f-endpoint to add a book
    @PostMapping("/addbook")
    public ResponseEntity<?> addBook(@RequestBody BookDto bookDto) {
        try {
            documentService.addBook(bookDto);
            return ResponseEntity.ok("Book added successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid book details: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while adding the book: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the book: " + e.getMessage());
        }
    }

    // f-endpoint to add a magazine
    @PostMapping("/addmagazine")
    public ResponseEntity<?> addMagazine(@RequestBody MagazineDto magazineDto) {
        try {
            documentService.addMagazine(magazineDto);
            return ResponseEntity.ok("Magazine added successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid magazine details: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while adding the magazine: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the magazine: " + e.getMessage());
        }
    }

    // f-endpoint to add a journal
    @PostMapping("/addjournal")
    public ResponseEntity<?> addJournal(@RequestBody JournalDto journalDto) {
        try {
            documentService.addJournal(journalDto);
            return ResponseEntity.ok("Journal added successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid journal details: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while adding the journal: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the journal: " + e.getMessage());
        }
    }

    // f-endpoint for fetch all documents of a specific type
    @GetMapping("/searchdocuments/{type}")
    public ResponseEntity<?> searchDocuments(@PathVariable String type) {
        try {
            List<DocumentResponseDto> documents = documentService.findDocumentsByType(type);
            return ResponseEntity.ok(documents);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred while fetching documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching documents: " + e.getMessage());
        }
    }

    // f-endpoint to update a document of a specific type
    @PutMapping("/searchdocuments/{type}/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable String type,
                                            @PathVariable String id,
                                            @RequestBody Map<String, Object> documentData) {
        try {
            Object updatedDocument = documentService.updateDocument(type, id, documentData);
            return ResponseEntity.ok(updatedDocument);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("An error occurred while updating the document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the document: " + e.getMessage());
        }
    }

    @PutMapping("/updatecopies/{type}/{id}")
    public ResponseEntity<?> updateCopies(
            @PathVariable String type,
            @PathVariable String id,
            @RequestBody Map<String, Integer> copiesData) {
        try {
            Integer newCopies = copiesData.get("copies");
            if (newCopies == null) {
                return ResponseEntity.badRequest().body("Missing 'copies' field in request body");
            }
            documentService.updateAvailableCopies(id, newCopies);
            return ResponseEntity.ok("Copies updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating copies: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating copies: " + e.getMessage());
        }
    }

    // f-endpoint to delete a document of a specific type
    @DeleteMapping("/searchdocuments/{type}/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String type, @PathVariable String id) {
        try {
            documentService.deleteDocument(type, id);
            return ResponseEntity.ok().body("Document deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("An error occurred while deleting the document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the document: " + e.getMessage());
        }
    }

    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(@RequestParam String containerName, @RequestParam MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            String imageUrl = this.imageStorageClient.uploadImage(containerName, file.getOriginalFilename(), inputStream, file.getSize());
            return ResponseEntity.ok().body(imageUrl);
        }
    }
}