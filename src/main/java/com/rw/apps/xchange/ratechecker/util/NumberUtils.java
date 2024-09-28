package com.rw.apps.xchange.ratechecker.util;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtils {
    // a greater than b
    public boolean gt(String a, String b) {
        return asDecimal(a).compareTo(asDecimal(b)) > 0;
    }

    public BigDecimal asDecimal(String v) {
        return new BigDecimal(v);
    }
}
