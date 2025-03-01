package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MagazineDto extends DocumentDto {

    private String publisher;
    private String isbn;
    private String title;
    private int year;
    private int month;
    private String imageUrl;

}