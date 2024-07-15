package com.codefirstgirls.lesson9.models.request;

import lombok.Data;

@Data
public class CreateBookRequest {
    private String title;
    private String author;
}
