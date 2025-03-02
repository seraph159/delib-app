package com.delibrary.lib_backend.controller;

import com.delibrary.lib_backend.dto.AccountInfoDto;
import com.delibrary.lib_backend.dto.BorrowDurationRequest;
import com.delibrary.lib_backend.dto.CreditCardDto;
import com.delibrary.lib_backend.dto.DocumentSearchDto;
import com.delibrary.lib_backend.entity.Document;
import com.delibrary.lib_backend.exception.*;
import com.delibrary.lib_backend.jwt.AuthUtils;
import com.delibrary.lib_backend.service_client.ClientDocumentService;
import com.delibrary.lib_backend.service_client.CreditCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController {

    @Autowired
    private ClientDocumentService clientDocumentService;

    @Autowired
    private CreditCardService creditCardService;

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @PostMapping("/search")
    public ResponseEntity<?> searchDocuments(
            @RequestBody DocumentSearchDto searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Document> documents = clientDocumentService.searchDocuments(searchDTO, pageable);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error searching documents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while searching documents.");
        }
    }

    @PutMapping("/{documentId}/borrow")
    public ResponseEntity<?> borrowDocument(
            @PathVariable String documentId,
            @RequestBody BorrowDurationRequest durationRequest) {
        try {
            String clientEmail = AuthUtils.getAuthenticatedClientEmail();
            Document borrowedDocument = clientDocumentService.borrowDocument(documentId, clientEmail, durationRequest);
            return ResponseEntity.ok(borrowedDocument);
        } catch (DocumentNotFoundException | ClientNotFoundException e) {
            logger.error("Borrowing error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DocumentNotAvailableException e) {
            logger.warn("Document not available: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during borrowing", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/account-info")
    public ResponseEntity<?> getAccountInfo() {
        try {
            String clientEmail = AuthUtils.getAuthenticatedClientEmail();
            AccountInfoDto accountInfo = clientDocumentService.getAccountInfo(clientEmail);
            return ResponseEntity.ok(accountInfo);
        } catch (ClientNotFoundException e) {
            logger.error("Client not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error fetching account info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching account information.");
        }
    }

    @GetMapping("/credit-card")
    public ResponseEntity<?> getCreditCard() {
        try {
            String clientEmail = AuthUtils.getAuthenticatedClientEmail();
            CreditCardDto creditCard = creditCardService.getCreditCard(clientEmail);
            return ResponseEntity.ok(creditCard);
        } catch (CreditCardNotFoundException e) {
            logger.warn("Credit card not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error fetching credit card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching credit card information.");
        }
    }

    @PostMapping("/credit-card")
    public ResponseEntity<?> addCreditCard(@RequestBody CreditCardDto creditCardDTO) {
        try {
            String clientEmail = AuthUtils.getAuthenticatedClientEmail();
            creditCardService.addOrEditCreditCard(clientEmail, creditCardDTO);
            return ResponseEntity.ok("Card Registered Successfully!");
        } catch (InvalidCreditCardException e) {
            logger.warn("Invalid credit card: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding credit card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding credit card.");
        }
    }

    @PutMapping("/credit-card")
    public ResponseEntity<?> editCreditCard(@RequestBody CreditCardDto creditCardDTO) {
        try {
            String clientEmail = AuthUtils.getAuthenticatedClientEmail();
            creditCardService.addOrEditCreditCard(clientEmail, creditCardDTO);
            return ResponseEntity.ok(creditCardDTO);
        } catch (CreditCardNotFoundException e) {
            logger.warn("Credit card not found for editing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error editing credit card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error editing credit card.");
        }
    }

    @DeleteMapping("/credit-card")
    public ResponseEntity<?> deleteCreditCard() {
        try {
            String clientEmail = AuthUtils.getAuthenticatedClientEmail();
            creditCardService.deleteCreditCard(clientEmail);
            return ResponseEntity.ok("Card Deleted Successfully!");
        } catch (CreditCardNotFoundException e) {
            logger.warn("Credit card not found for deletion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting credit card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting credit card.");
        }
    }
}
