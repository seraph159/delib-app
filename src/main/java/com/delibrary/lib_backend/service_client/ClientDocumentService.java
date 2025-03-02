package com.delibrary.lib_backend.service_client;

import com.delibrary.lib_backend.dto.AccountInfoDto;
import com.delibrary.lib_backend.dto.BorrowDurationRequest;
import com.delibrary.lib_backend.dto.DocumentSearchDto;
import com.delibrary.lib_backend.entity.*;
import com.delibrary.lib_backend.exception.ClientNotFoundException;
import com.delibrary.lib_backend.exception.DocumentNotAvailableException;
import com.delibrary.lib_backend.exception.DocumentNotFoundException;
import com.delibrary.lib_backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class ClientDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BorrowRecordCopiesRepository borrowRecordCopiesRepository;

    private static final double OVERDUE_FEE_PER_DAY = 0.50; // $0.50 per day

    public Page<Document> searchDocuments(DocumentSearchDto searchDTO, Pageable pageable) {

        // Validate if DTO contains any filtering criteria
        if (isDtoEmpty(searchDTO)) {
            return Page.empty(pageable); // Return an empty page if no criteria
        }

        DocumentSpecification spec = new DocumentSpecification(searchDTO);
        return documentRepository.findAll(spec, pageable);
    }

    private boolean isDtoEmpty(DocumentSearchDto dto) {
        return (dto.getTitle() == null || dto.getTitle().isEmpty()) &&
                (dto.getIsbn() == null || dto.getIsbn().isEmpty()) &&
                dto.getYear() == null &&
                (dto.getPublisher() == null || dto.getPublisher().isEmpty()) &&
                !dto.isOnlyAvailable();
    }

    public Document borrowDocument(String documentId, String clientEmail, BorrowDurationRequest durationRequest) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        Client client = clientRepository.findById(clientEmail)
                .orElseThrow(() -> new ClientNotFoundException(clientEmail));

        // Check for existing active borrow record
        Optional<BorrowRecordCopies> existingBorrow = borrowRecordCopiesRepository
                .findByClientAndDocumentAndReturnDateIsNull(client, document);

        if (existingBorrow.isPresent()) {
            throw new IllegalStateException("Client " + clientEmail + " has already borrowed document " + documentId + " and it has not been returned.");
        }

        if (document.getAvailableCopies() > 0) {
            document.setAvailableCopies(document.getAvailableCopies() - 1);
            document.setNoOfBorrowers(document.getNoOfBorrowers() + 1);

            BorrowRecordCopies borrowRecord = new BorrowRecordCopies();
            borrowRecord.setDocument(document);
            borrowRecord.setClient(clientRepository.findById(clientEmail).orElseThrow());
            borrowRecord.setBorrowDate(LocalDate.now());

            // Calculate due date based on duration
            LocalDate dueDate;
            int value = durationRequest != null ? durationRequest.getValue() : 4; // Default to 4 weeks
            String unit = durationRequest != null ? durationRequest.getUnit().toLowerCase() : "weeks";
            dueDate = switch (unit) {
                case "days", "day" -> LocalDate.now().plusDays(value);
                case "weeks", "week" -> LocalDate.now().plusWeeks(value);
                default -> throw new IllegalArgumentException("Invalid duration unit: " + unit);
            };
            borrowRecord.setDueDate(dueDate);
            borrowRecord.setReturnDate(null);

            borrowRecordCopiesRepository.save(borrowRecord);
            return documentRepository.save(document);
        } else {
            throw new DocumentNotAvailableException(documentId);
        }
    }

    public Document returnDocument(String documentId, String clientEmail) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        Client client = clientRepository.findById(clientEmail)
                .orElseThrow(() -> new ClientNotFoundException(clientEmail));

        // Find all active borrow records (returnDate is null)
        List<BorrowRecordCopies> borrowRecords = borrowRecordCopiesRepository
                .findAllByClientAndDocumentAndReturnDateIsNull(client, document);

        if (borrowRecords.isEmpty()) {
            throw new EntityNotFoundException("No active borrow record found for document " + documentId + " and client " + clientEmail);
        }

        // If multiple active borrows exists, return the oldest borrow record (based on borrowDate)
        BorrowRecordCopies borrowRecord = borrowRecords.stream()
                .min(Comparator.comparing(BorrowRecordCopies::getBorrowDate))
                .orElseThrow(() -> new EntityNotFoundException("Error processing borrow records"));

        // Mark as returned
        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecordCopiesRepository.save(borrowRecord);

        // Update document availability
        document.setAvailableCopies(document.getAvailableCopies() + 1);
        document.setNoOfBorrowers(document.getNoOfBorrowers() - 1);

        return documentRepository.save(document);
    }

    public AccountInfoDto getAccountInfo(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        // Handle ClientAddress null safety
        String address = "";
        if (client.getClientAddress() != null && client.getClientAddress().getAddress() != null) {
            address = client.getClientAddress().getAddress();
        }

        // Get borrowed document IDs
        List<String> documentsBorrowed = client.getBorrowRecordCopies().stream()
                .map(borrowRecord -> borrowRecord.getDocument().getDocumentId())
                .collect(Collectors.toList());

        // Calculate overdue fees
        List<AccountInfoDto.OverdueDocument> overdueDocuments = new ArrayList<>();
        double totalOverdueFees = 0.0;

        LocalDate currentDate = LocalDate.now();
        for (BorrowRecordCopies record : client.getBorrowRecordCopies()) {
            LocalDate dueDate = record.getDueDate();
            if (dueDate != null && dueDate.isBefore(currentDate)) {
                int overdueDays = (int) ChronoUnit.DAYS.between(dueDate, currentDate);
                double fee = overdueDays * OVERDUE_FEE_PER_DAY;
                totalOverdueFees += fee;

                overdueDocuments.add(new AccountInfoDto.OverdueDocument(
                        record.getDocument().getDocumentId(),
                        overdueDays,
                        fee
                ));
            }
        }

        return new AccountInfoDto(
                client.getName(),
                client.getEmail(),
                address,
                documentsBorrowed,
                totalOverdueFees,
                overdueDocuments.isEmpty() ? null : overdueDocuments // Omit if no overdue documents
        );
    }
}