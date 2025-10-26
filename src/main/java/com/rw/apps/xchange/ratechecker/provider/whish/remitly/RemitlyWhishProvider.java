package com.rw.apps.xchange.ratechecker.provider.whish.remitly;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.whish.remitly.model.RemitlyEstimate;
import com.rw.apps.xchange.ratechecker.provider.whish.remitly.model.RemitlyEstimates;
import com.rw.apps.xchange.ratechecker.provider.whish.remitly.util.EffectiveRateCalculator;
import java.math.BigDecimal;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RemitlyWhishProvider implements ExchangeRateProvider {
    private final RestTemplate restTemplate;

    public RemitlyWhishProvider() {
        restTemplate = new RestTemplateBuilder()
                .rootUri("https://api.remitly.io")
                .build();
    }

    @Override
    public ExchangeRate getEurToUsdExchangeRate() {
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/v3/calculator/estimate")
                                                          .queryParam("conduit", "NLD:EUR-LBN:USD")
                                                          .queryParam("amount", "100")
                                                          .queryParam("anchor", "SEND")
                                                          .queryParam("purpose", "OTHER")
                                                          .queryParam("customer_segment", "UNRECOGNIZED")
                                                          .queryParam("strict_promo", "false").build();
        ResponseEntity<RemitlyEstimates> responseEntity = restTemplate.exchange(uriComponents.toUriString(),
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                RemitlyEstimates.class);
        RemitlyEstimate remitlyEstimate = responseEntity.getBody().estimate();
        BigDecimal exchangeRate = new BigDecimal(remitlyEstimate.exchangeRate().baseRate());
        String effectiveRate = EffectiveRateCalculator.calculateEffectiveRate(exchangeRate);
        return new ExchangeRate(Currency.EURO.getCode(), Currency.US_DOLLARS.getCode(), effectiveRate);
    }
}
