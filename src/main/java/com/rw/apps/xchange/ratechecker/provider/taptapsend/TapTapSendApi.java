package com.rw.apps.xchange.ratechecker.provider.taptapsend;

import com.rw.apps.xchange.ratechecker.model.Country;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.model.FromCountryFxRates;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.model.FxRatesResponse;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.model.ToCountryFxRate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TapTapSendApi {
    private final RestTemplate restTemplate;

    public TapTapSendApi() {
        restTemplate = new RestTemplateBuilder()
                .rootUri("https://api.taptapsend.com")
                .defaultHeader("X-Device-Id", "web")
                .defaultHeader("X-Device-Model", "web")
                .defaultHeader("Appian-Version", "web/2022-05-03.0")
                .build();
    }

    public ExchangeRate getEurToUsdExchangeRate() {
        FxRatesResponse fxRates = restTemplate.getForObject("/api/fxRates", FxRatesResponse.class);
        FromCountryFxRates fromCountryFxRates = fxRates.getFxRatesForCountry(Country.NETHERLANDS.getCode());
        ToCountryFxRate toCountryFxRate = fromCountryFxRates.getToCountryFxRate(Country.LEBANON.getCode());
        return ModelMapper.toExchangeRate(fromCountryFxRates, toCountryFxRate);
    }
}
