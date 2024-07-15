package com.codefirstgirls.lesson9.models.request;

import lombok.Data;

@Data
public class CreateLoanRequest {
    private Long bookId;
    private Long customerId;
}
