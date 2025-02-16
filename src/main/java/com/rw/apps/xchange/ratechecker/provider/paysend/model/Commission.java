package com.rw.apps.xchange.ratechecker.provider.paysend.model;

import java.math.BigDecimal;

public record Commission(BigDecimal convertRate, BigDecimal fee) {
}
