package com.rw.apps.xchange.ratechecker.provider.openerapi;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.openerapi.model.OpenRateResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenErApi {
    private final RestTemplate restTemplate;

    public OpenErApi() {
        restTemplate = new RestTemplateBuilder()
                .rootUri("https://open.er-api.com/v6/latest")
                .build();
    }

    public ExchangeRate getEurToUsdExchangeRate() {
        OpenRateResponse openRate = getRatesForCurrency(Currency.EURO.getCode());
        return new ExchangeRate(openRate.baseCurrencyCode(),
                                Currency.US_DOLLARS.getCode(),
                                String.valueOf(openRate.rates().get(Currency.US_DOLLARS.getCode())));
    }

    private OpenRateResponse getRatesForCurrency(String currency) {
        return restTemplate.getForObject("/" + currency, OpenRateResponse.class);
    }
}
