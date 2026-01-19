package com.rw.apps.xchange.ratechecker.provider.paysend;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.paysend.model.PaySendRates;
import com.rw.apps.xchange.ratechecker.provider.paysend.util.EffectiveRateCalculator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

// @Component
public class PaySendApi implements ExchangeRateProvider {
    private final RestTemplate restTemplate;

    public PaySendApi() {
        restTemplate = new RestTemplateBuilder().rootUri("https://paysend.com").build();
    }

    @Override
    public String getName() {
        return "PaySend";
    }

    @Override
    public int getOrder() {
        return 1000;
    }

    @Override
    public ExchangeRate getEurToUsdExchangeRate() {
        PaySendRates rateAndFee = restTemplate.postForObject(
                "/api/en-nl/send-money/from-netherlands-to-lebanon?fromCurrId=978&toCurrId=840&isFrom=true",
                null, PaySendRates.class);
        return new ExchangeRate(Currency.EURO.getCode(), Currency.US_DOLLARS.getCode(),
                EffectiveRateCalculator
                        .calculateEffectiveRate(rateAndFee.commission().convertRate()));
    }
}
