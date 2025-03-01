package com.delibrary.lib_backend.dto;

import lombok.Data;

@Data
public class AuthResponseDto {

    private String accessToken;
    private String role;

}
