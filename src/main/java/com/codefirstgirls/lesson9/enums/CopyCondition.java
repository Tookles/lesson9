package com.codefirstgirls.lesson9.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CopyCondition {
    POOR("poor"),
    OK("ok"),
    GOOD("good"),
    EXCELLENT("excellent");

    private String condition;

    private CopyCondition(String condition) {
        this.condition = condition;
    }

    @JsonValue
    public String getCondition() {
        return condition;
    }
}
