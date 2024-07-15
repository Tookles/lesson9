package com.codefirstgirls.lesson9.models.request;

import com.codefirstgirls.lesson9.enums.CopyCondition;
import lombok.Data;

@Data
public class CreateCopyRequest {
    private Long bookId;
    private CopyCondition condition;
}
