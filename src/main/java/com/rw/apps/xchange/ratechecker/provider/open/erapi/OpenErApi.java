package com.rw.apps.xchange.ratechecker.provider.open.erapi;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.open.erapi.model.OpenRateResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

// @Component
public class OpenErApi implements ExchangeRateProvider {
    private final RestTemplate restTemplate;

    public OpenErApi() {
        restTemplate = new RestTemplateBuilder()
                .rootUri("https://open.er-api.com/v6/latest")
                .build();
    }

    @Override
    public ExchangeRate getEurToUsdExchangeRate() {
        OpenRateResponse openRate = getRatesForCurrency(Currency.EURO.getCode());
        return new ExchangeRate(openRate.baseCurrencyCode(),
                Currency.US_DOLLARS.getCode(),
                String.valueOf(openRate.rates().get(Currency.US_DOLLARS.getCode())));
    }

    private OpenRateResponse getRatesForCurrency(String currency) {
        return restTemplate.getForObject("/" + currency, OpenRateResponse.class);
    }

    @Override
    public String getName() {
        return "Open ER API";
    }
}
