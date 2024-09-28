package com.rw.apps.xchange.ratechecker.graph;

import com.rw.apps.xchange.ratechecker.db.ExchangeRateComparison;
import com.rw.apps.xchange.ratechecker.util.FileHelper;
import java.awt.Color;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.springframework.stereotype.Component;

@Component
public class Grapher {
    private static final String TEMP_IMAGE_FILENAME = "temp.png";
    @Getter
    private final String tempImageFilePath;

    public Grapher(FileHelper fileHelper) {
        this.tempImageFilePath = fileHelper.resolve(TEMP_IMAGE_FILENAME).toString();
    }

    public void draw(List<ExchangeRateComparison> rateRecords) {
        XYChart chart = createChart(rateRecords);

        saveChartAsImageFile(chart);
    }

    private XYChart createChart(List<ExchangeRateComparison> rateRecords) {
        XYChart chart = new XYChartBuilder()
                .width(640)
                .height(480)
                .title("Exchange rate comparison")
                .xAxisTitle("Time")
                .yAxisTitle("Exchange rate EUR/USD")
                .theme(ChartTheme.Matlab)
                .build();

        chart.getStyler().setSeriesColors(new Color[] {
                new Color(233, 21, 21),
                new Color(2, 104, 31)});
        chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

        int size = rateRecords.size();
        List<Date> dateTimes = new ArrayList<>(size);
        List<Double> openRates = new ArrayList<>(size);
        List<Double> appRates = new ArrayList<>(size);
        for (var rateRecord : rateRecords) {
            dateTimes.add(Date.from(rateRecord.timestamp()));

            double openRate = Double.parseDouble(rateRecord.openRate());
            openRates.add(openRate);

            double appRate = Double.parseDouble(rateRecord.appRate());
            appRates.add(appRate);
        }

        chart.addSeries("Open rate", dateTimes, openRates);
        chart.addSeries("TapTapSend rate", dateTimes, appRates);

        return chart;
    }

    private void saveChartAsImageFile(XYChart chart) {
        try {
            BitmapEncoder.saveBitmap(chart,
                                     tempImageFilePath,
                                     BitmapFormat.PNG);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
