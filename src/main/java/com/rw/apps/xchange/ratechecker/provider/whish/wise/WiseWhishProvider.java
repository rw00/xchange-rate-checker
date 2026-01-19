package com.rw.apps.xchange.ratechecker.provider.whish.wise;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.whish.wise.model.WiseRate;
import com.rw.apps.xchange.ratechecker.provider.whish.wise.util.EffectiveRateCalculator;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WiseWhishProvider implements ExchangeRateProvider {
    private final RestTemplate restTemplate;

    public WiseWhishProvider(@Value("${provider.wise.token}") String token) {
        restTemplate = new RestTemplateBuilder().rootUri("https://api.wise.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    @Override
    public String getName() {
        return "Wise Whish";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public ExchangeRate getEurToUsdExchangeRate() {
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/v1/rates")
                .queryParam("source", "EUR").queryParam("target", "USD").build();
        ResponseEntity<List<WiseRate>> responseEntity =
                restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});
        WiseRate wiseRate = responseEntity.getBody().get(0);
        String effectiveRate = EffectiveRateCalculator.calculateEffectiveRate(wiseRate.rate());
        return new ExchangeRate(Currency.EURO.getCode(), Currency.US_DOLLARS.getCode(),
                effectiveRate);
    }
}
