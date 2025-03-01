package com.delibrary.lib_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private boolean isElectronic;
    //private int totalNoOfCopies;
    private int noOfBorrowers;
    private int availableCopies;
    private LocalDate earliestAvailableDate;
}