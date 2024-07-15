package com.codefirstgirls.lesson9.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoanStatus {
    COMPLETED("completed"),
    ACTIVE("active"),
    OVERDUE("overdue");

    private String status;

    private LoanStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
