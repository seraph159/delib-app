package com.delibrary.lib_backend.service_client;

import com.delibrary.lib_backend.dto.AccountInfoDto;
import com.delibrary.lib_backend.dto.DocumentSearchDto;
import com.delibrary.lib_backend.entity.*;
import com.delibrary.lib_backend.exception.ClientNotFoundException;
import com.delibrary.lib_backend.exception.DocumentNotAvailableException;
import com.delibrary.lib_backend.exception.DocumentNotFoundException;
import com.delibrary.lib_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    public Document borrowDocument(String documentId, String clientEmail) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        if (document.getAvailableCopies() > 0) {
            document.setAvailableCopies(document.getAvailableCopies() - 1);
            document.setNoOfBorrowers(document.getNoOfBorrowers() + 1);

            BorrowRecordCopies borrowRecord = new BorrowRecordCopies();
            borrowRecord.setDocument(document);
            borrowRecord.setClient(clientRepository.findById(clientEmail).orElseThrow());
            borrowRecord.setBorrowDate(LocalDate.now());
            borrowRecord.setDueDate(LocalDate.now().plusWeeks(4));
            borrowRecordCopiesRepository.save(borrowRecord);

            return documentRepository.save(document);
        } else {
            throw new DocumentNotAvailableException(documentId);
        }
    }

    public AccountInfoDto getAccountInfo(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        // Handle ClientAddress null safety
        String address = "";
        if (client.getClientAddress() != null && client.getClientAddress().getAddress() != null) {
            address = client.getClientAddress().getAddress();
        }

        List<String> documentsBorrowed = client.getBorrowRecordCopies().stream()
                .map(borrowRecord -> borrowRecord.getDocument().getDocumentId())
                .collect(Collectors.toList());

        return new AccountInfoDto(
                client.getName(),
                client.getEmail(),
                address,
                documentsBorrowed
        );
    }
}