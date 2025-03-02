package com.delibrary.lib_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {
    private String name;
    private String email;
    private String address;
    private List<String> documentsBorrowed;
    private double totalOverdueFees; // New field for total overdue fees
    private List<OverdueDocument> overdueDocuments; // Optional: detailed overdue info

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OverdueDocument {
        private String documentId;
        private int overdueDays;
        private double fee;
    }
}