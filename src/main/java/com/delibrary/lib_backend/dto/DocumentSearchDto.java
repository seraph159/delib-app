package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchDto {
    private String title;
    private String author;
    private String isbn;
    private Integer year;
    private String publisher;
    private String sortBy;
    private Integer maxResults;
    private boolean onlyAvailable;
}