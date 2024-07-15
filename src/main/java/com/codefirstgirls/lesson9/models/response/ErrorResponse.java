package com.codefirstgirls.lesson9.models.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private List<String> errors = new ArrayList<String>();
}
