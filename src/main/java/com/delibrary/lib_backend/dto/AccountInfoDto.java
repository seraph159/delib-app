package com.delibrary.lib_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {
    private String name;
    private String email;
    private String address;
    private List<String> documentsBorrowed;
}