package com.rw.apps.xchange.ratechecker;

import com.rw.apps.xchange.ratechecker.db.ExchangeRateComparison;
import com.rw.apps.xchange.ratechecker.db.FileDb;
import com.rw.apps.xchange.ratechecker.db.LastValueDb;
import com.rw.apps.xchange.ratechecker.graph.Grapher;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.openerapi.OpenErApi;
import com.rw.apps.xchange.ratechecker.provider.taptapsend.TapTapSendApi;
import com.rw.apps.xchange.ratechecker.util.DateTimeUtils;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class RateCheckerService {
    private final OpenErApi openErApi;
    private final TapTapSendApi tapTapSendApi;
    private final LastValueDb lastValueDb;
    private final FileDb fileDb;
    private final Grapher grapher;

    public boolean check() throws Exception {
        ExchangeRate openRate = openErApi.getEurToUsdExchangeRate();
        ExchangeRate tapTapSendRate = tapTapSendApi.getEurToUsdExchangeRate();

        var rateRecord = new ExchangeRateComparison(openRate.fxRate(),
                                                    tapTapSendRate.fxRate(),
                                                    DateTimeUtils.now());

        if (lastValueDb.updateIfGreater(rateRecord)) {
            fileDb.persistRecord(rateRecord);

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
