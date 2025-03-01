package com.delibrary.lib_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "librarians")
public class Librarian extends User {

    @Column(name = "ssn",  nullable = false, unique = true)
    private String ssn;

    @Column(nullable = false)
    private Integer salary;

}