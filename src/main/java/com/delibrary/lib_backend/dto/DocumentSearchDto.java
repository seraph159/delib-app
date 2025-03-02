package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchDto {
    private String title;
    private String author;
    private String isbn;
    private Integer year;
    private String publisher;
    private String sortBy; // e.g., "title", "year", "publisher", "availableCopies"
    private String sortDirection; // "asc" or "desc" (optional, default to "asc")
    private Integer maxResults;
    private boolean onlyAvailable;
}