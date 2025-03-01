package com.delibrary.lib_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @OneToOne(mappedBy = "creditCard")
    private Client client;
}