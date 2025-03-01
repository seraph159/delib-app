package com.delibrary.lib_backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "documentId"
//)
@Table(name = "document")
public class Document {

    @Id
    private String documentId;

    @Column(nullable = false)
    private boolean isElectronic;

//    @Column(nullable = false)
//    private int totalNoOfCopies;

    @Column(nullable = false)
    private int noOfBorrowers;

    private int availableCopies;

    private LocalDate earliestAvailableDate;

    // Infinite recursion
    // @OneToMany(mappedBy = "document")
    // private List<BorrowRecordCopies> borrowRecordCopies;

    @OneToOne(optional = true)
    @JoinColumn(name = "book_id", nullable = true)
    private Book book;

    @OneToOne(optional = true)
    @JoinColumn(name = "magazine_id", nullable = true)
    private Magazine magazine;

    @OneToOne(optional = true)
    @JoinColumn(name = "journal_id", nullable = true)
    private JournalArticle journalArticle;

}
