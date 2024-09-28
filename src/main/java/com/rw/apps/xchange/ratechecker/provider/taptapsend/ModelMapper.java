package com.rw.apps.xchange.ratechecker.provider.taptapsend;

import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.model.FromCountryFxRates;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.model.ToCountryFxRate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModelMapper {
    public ExchangeRate toExchangeRate(FromCountryFxRates fromCountryFxRates, ToCountryFxRate toCountryFxRate) {
        return new ExchangeRate(fromCountryFxRates.currency(), toCountryFxRate.currency(), toCountryFxRate.rate());
    }
}
