package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalDto extends DocumentDto {

    private String title;
    private String journal;
    private int number;
    private int year;
    private int issue;
    private String publisher;
    private String imageUrl;

}