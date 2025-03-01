package com.delibrary.lib_backend.service_client;

import com.delibrary.lib_backend.dto.CreditCardDto;
import com.delibrary.lib_backend.entity.Client;
import com.delibrary.lib_backend.entity.CreditCard;
import com.delibrary.lib_backend.exception.ClientNotFoundException;
import com.delibrary.lib_backend.exception.CreditCardNotFoundException;
import com.delibrary.lib_backend.repository.ClientRepository;
import com.delibrary.lib_backend.repository.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ClientRepository clientRepository;


    public CreditCardDto getCreditCard(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        if (client.getCreditCard() == null || client.getCreditCard().getCardNo() == null) {
            throw new CreditCardNotFoundException("Credit card not found");
        }

        String cardNo = client.getCreditCard().getCardNo();

        return creditCardRepository.findById(cardNo)
                .map(card -> new CreditCardDto(card.getCardNo(), card.getCardHolderName()))
                .orElseThrow(() -> new CreditCardNotFoundException("Credit card not found"));
    }

    public void addOrEditCreditCard(String email, CreditCardDto creditCardDto) {
        Client client = clientRepository.findById(email)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // Remove the old CreditCard if needed
        if (client.getCreditCard() == null) {

            CreditCard newCreditCard = new CreditCard();
            newCreditCard.setCardNo(creditCardDto.getCardNo());
            newCreditCard.setCardHolderName(creditCardDto.getCardHolderName());

            // Update the CreditCard reference
            client.setCreditCard(newCreditCard);
        } else {
            CreditCard newCreditCard = client.getCreditCard();
            newCreditCard.setCardNo(creditCardDto.getCardNo());
            newCreditCard.setCardHolderName(creditCardDto.getCardHolderName());
        }


        // Persist changes
        clientRepository.save(client);
    }


    public void deleteCreditCard(String email) {
        // Find the client associated with the credit card
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new CreditCardNotFoundException("Credit card not found"));

        // Remove the association
        client.setCreditCard(null);

        // Save the updated client
        clientRepository.save(client);
    }
}
