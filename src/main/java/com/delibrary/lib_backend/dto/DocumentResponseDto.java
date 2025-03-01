package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto {
    private Long copyId;
    private String documentId;
    private String title; // For Book and JournalArticle; null for Magazine
    private String name;  // For Magazine; null for others
    private String publisher;
    private String isbn;  // For Book and Magazine; null for JournalArticle
    private int edition;  // For Book; 0 for others
    private int year;
    private int month;    // For Magazine; 0 for others
    private String journal; // For JournalArticle; null for others
    private int number;   // For JournalArticle; 0 for others
    private int issue;    // For JournalArticle; 0 for others
    private int availableCopies;
    private String imageUrl;
}
