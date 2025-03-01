package com.delibrary.lib_backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "email"
//)
@Table(name = "clients")
public class Client extends User {

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "email")
    private ClientAddress clientAddress;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "card_no")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "client")
    private List<BorrowRecordCopies> borrowRecordCopies;

}