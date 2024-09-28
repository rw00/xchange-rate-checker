package com.rw.apps.xchange.ratechecker.model;

import lombok.Getter;

public enum Currency {
    EURO("EUR"),
    US_DOLLARS("USD"),
    ;

    @Getter
    private final String code;

    Currency(String code) {
        this.code = code;
    }
}
