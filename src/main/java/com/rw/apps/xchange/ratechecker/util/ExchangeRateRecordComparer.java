package com.rw.apps.xchange.ratechecker.util;

import com.rw.apps.xchange.ratechecker.db.ExchangeRateComparison;
import com.rw.apps.xchange.ratechecker.db.LastValuesHolder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExchangeRateRecordComparer {
    public boolean shouldUpdate(LastValuesHolder lastValues, ExchangeRateComparison current) {
        return false;
    }
}
