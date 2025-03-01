package com.delibrary.lib_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "journal_article")
public class JournalArticle {

    @Id
    @GeneratedValue
    private Long copyId;

    @Column(nullable = false)
    private String documentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String journal;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int issue;

    @Column(nullable = false)
    private String publisher;

    private String imageUrl;

}