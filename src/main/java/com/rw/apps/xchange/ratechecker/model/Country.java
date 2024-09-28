package com.rw.apps.xchange.ratechecker.model;

import lombok.Getter;

public enum Country {
    NETHERLANDS("NL"),
    LEBANON("LB");

    @Getter
    private final String code;

    Country(String code) {
        this.code = code;
    }
}
