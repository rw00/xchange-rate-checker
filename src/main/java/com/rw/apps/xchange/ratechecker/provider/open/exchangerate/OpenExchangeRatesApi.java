package com.rw.apps.xchange.ratechecker.provider.open.exchangerate;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.open.exchangerate.model.OpenRateResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenExchangeRatesApi implements ExchangeRateProvider {
    private final RestTemplate restTemplate;
    private final String appId;

    public OpenExchangeRatesApi(@Value("${provider.open-exchangerates.app-id}") String appId) {
        this.appId = appId;
        restTemplate = new RestTemplateBuilder()
                .rootUri("https://openexchangerates.org/api")
                .build();
    }

    @Override
    public String getName() {
        return "Open rate";
    }

    @Override
    public ExchangeRate getEurToUsdExchangeRate() {
        OpenRateResponse openRate = getRatesForCurrency(Currency.EURO.getCode());
        BigDecimal rate = BigDecimal.ONE.divide(BigDecimal.valueOf(openRate.rates().get(Currency.EURO.getCode())),
                2,
                RoundingMode.HALF_UP);
        return new ExchangeRate(openRate.baseCurrencyCode(),
                Currency.US_DOLLARS.getCode(),
                String.valueOf(rate));
    }

    private OpenRateResponse getRatesForCurrency(String currency) {
        Map<String, String> params = Map.of("app_id", appId, "symbols", currency);
        return restTemplate.getForObject("/latest.json?app_id={app_id}&symbols={symbols}",
                OpenRateResponse.class,
                params);
    }
}
