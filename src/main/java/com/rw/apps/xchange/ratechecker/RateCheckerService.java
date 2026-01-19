package com.rw.apps.xchange.ratechecker;

import com.rw.apps.xchange.ratechecker.db.ExchangeRateComparison;
import com.rw.apps.xchange.ratechecker.db.FileDb;
import com.rw.apps.xchange.ratechecker.db.ProviderRateAndSpread;
import com.rw.apps.xchange.ratechecker.graph.Grapher;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.open.exchangerate.OpenExchangeRatesApi;
import com.rw.apps.xchange.ratechecker.util.DateTimeUtils;
import com.rw.apps.xchange.ratechecker.util.ExchangeRateApiCaller;
import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class RateCheckerService {
    private final OpenExchangeRatesApi openRateProvider;
    private final List<ExchangeRateProvider> providers;
    private final FxImprovementDetector improvementDetector;
    private final FileDb fileDb;
    private final Grapher grapher;

    public RateCheckerService(OpenExchangeRatesApi openRateProvider,
            List<ExchangeRateProvider> providers, FxImprovementDetector improvementDetector,
            FileDb fileDb, Grapher grapher) {
        this.openRateProvider = openRateProvider;
        this.providers = providers.stream()
                .sorted(Comparator.comparingInt(ExchangeRateProvider::getOrder)).toList();
        this.improvementDetector = improvementDetector;
        this.fileDb = fileDb;
        this.grapher = grapher;
    }

    public boolean check() throws Exception {
        return check(false);
    }

    public boolean check(boolean runAnyway) throws Exception {
        ExchangeRate openRate = ExchangeRateApiCaller.call(openRateProvider);
        double openRateVal = Double.parseDouble(openRate.fxRate());

        Map<String, String> providerRates = new LinkedHashMap<>();
        Map<String, ProviderRateAndSpread> providerData = new LinkedHashMap<>();

        for (ExchangeRateProvider provider : providers) {
            if (provider == openRateProvider) {
                continue;
            }
            try {
                ExchangeRate exchangeRate = ExchangeRateApiCaller.call(provider);
                String rateStr = exchangeRate.fxRate();
                double providerRateVal = Double.parseDouble(rateStr);
                double spread = openRateVal - providerRateVal;

                providerRates.put(provider.getName(), rateStr);
                providerData.put(provider.getName(), new ProviderRateAndSpread(rateStr, spread));
            } catch (Exception e) {
                log.error("Failed to fetch rate from provider: {}", provider.getName(), e);
            }
        }

        var exchangeRateComparison =
                new ExchangeRateComparison(openRate.fxRate(), providerRates, DateTimeUtils.now());

        ExchangeRateComparison lastRecord = fileDb.getLastRecord();
        Instant lastNotificationTime = lastRecord != null ? lastRecord.timestamp() : null;

        if (improvementDetector.shouldNotify(openRateVal, providerData, lastNotificationTime)
                || runAnyway) {
            fileDb.persistRecord(exchangeRateComparison);
            fileDb.clearOldRecords();
            return true;
        }
        return false;
    }

    public String saveGraph() throws IOException {
        List<ExchangeRateComparison> rateRecords = fileDb.readAll();
        grapher.draw(rateRecords);

        return grapher.getTempImageFilePath();
    }
}
