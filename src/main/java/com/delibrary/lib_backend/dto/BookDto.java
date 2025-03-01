package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto extends DocumentDto {

    private String title;
    private String publisher;
    private String isbn;
    private int edition;
    private int year;
    private String imageUrl;

}
