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
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "email"
//)
@Table(name = "client_address")
public class ClientAddress {

    @Id
    @Column(nullable = false)
    private String email;

    @Column(name = "streetAddress")
    private String address;

    private String city;

    @Column(name = "_state_")
    private String state;

    @Column(name = "zipCode")
    private String zipcode;

    @OneToOne(mappedBy = "clientAddress")
    private Client client;

}