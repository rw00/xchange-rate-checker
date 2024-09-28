package com.rw.apps.xchange.ratechecker.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {
    public Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
