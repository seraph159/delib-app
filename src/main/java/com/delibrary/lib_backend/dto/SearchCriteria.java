package com.delibrary.lib_backend.dto;


import lombok.Data;

@Data
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private String logicalOperator; // "AND" or "OR"

    // Getters and setters
}