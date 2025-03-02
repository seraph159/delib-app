package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegistrationDto {

    private String email;
    private String name;
    private String password;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private List<String> documentsBorrowed;

    public ClientRegistrationDto(String email, String name, String address, String city, String state, String zipcode) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

}