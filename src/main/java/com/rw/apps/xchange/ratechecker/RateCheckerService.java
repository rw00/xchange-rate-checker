package com.rw.apps.xchange.ratechecker;

import com.rw.apps.xchange.ratechecker.db.ExchangeRateComparison;
import com.rw.apps.xchange.ratechecker.db.FileDb;
import com.rw.apps.xchange.ratechecker.db.LastValueDb;
import com.rw.apps.xchange.ratechecker.graph.Grapher;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.open.exchangerate.OpenExchangeRatesApi;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.TapTapSendApi;
import com.rw.apps.xchange.ratechecker.provider.whish.remitly.RemitlyWhishProvider;
import com.rw.apps.xchange.ratechecker.provider.whish.wise.WiseWhishProvider;
import com.rw.apps.xchange.ratechecker.util.DateTimeUtils;
import com.rw.apps.xchange.ratechecker.util.ExchangeRateApiCaller;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class RateCheckerService {
    private final OpenExchangeRatesApi openExchangeRatesApi;
    private final TapTapSendApi tapTapSendApi;
    private final RemitlyWhishProvider remitlyWhishProvider;
    private final WiseWhishProvider wiseWhishProvider;
    private final LastValueDb lastValueDb;
    private final FileDb fileDb;
    private final Grapher grapher;

    public boolean check() throws Exception {
        return check(false);
    }

    public boolean check(boolean runAnyway) throws Exception {
        ExchangeRate openRate = ExchangeRateApiCaller.call(openExchangeRatesApi);
        ExchangeRate tapTapSendRate = ExchangeRateApiCaller.call(tapTapSendApi);
        ExchangeRate whishRemitlyRate = ExchangeRateApiCaller.call(remitlyWhishProvider);
        ExchangeRate whishWiseRate = ExchangeRateApiCaller.call(wiseWhishProvider);

        Map<String, String> providerRates = new LinkedHashMap<>();
        providerRates.put("TapTapSend", tapTapSendRate.fxRate());
        providerRates.put("Remitly Whish", whishRemitlyRate.fxRate());
        providerRates.put("Wise Whish", whishWiseRate.fxRate());

        var exchangeRate = new ExchangeRateComparison(openRate.fxRate(),
                providerRates,
                DateTimeUtils.now());

        if (lastValueDb.updateIfGreater(exchangeRate) || runAnyway) {
            fileDb.persistRecord(exchangeRate);
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
