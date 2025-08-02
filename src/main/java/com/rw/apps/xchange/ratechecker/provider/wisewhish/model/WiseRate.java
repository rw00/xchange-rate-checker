package com.rw.apps.xchange.ratechecker.provider.wisewhish.model;

import java.math.BigDecimal;

public record WiseRate(String source, String target, BigDecimal rate) {
}
