package com.rw.apps.xchange.ratechecker.graph;

import com.rw.apps.xchange.ratechecker.db.ExchangeRateComparison;
import com.rw.apps.xchange.ratechecker.util.FileHelper;
import java.awt.Color;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private static final Map<String, Color> providerColors = new LinkedHashMap<>();

    static {
        providerColors.put("Open rate", new Color(73, 80, 87)); //
        providerColors.put("Wise Whish rate", new Color(255, 10, 70)); //
        // providerColors.put("Remitly Whish rate", new Color(116, 61, 149)); //
        providerColors.put("TapTapSend rate", new Color(2, 104, 31));
    }

    public Grapher(FileHelper fileHelper) {
        this.tempImageFilePath = fileHelper.resolve(TEMP_IMAGE_FILENAME).toString();
    }

    public void draw(List<ExchangeRateComparison> rateRecords) {
        if (rateRecords.isEmpty()) {
            return;
        }
        XYChart chart = createChart(rateRecords);
        saveChartAsImageFile(chart);
    }

    private XYChart createChart(List<ExchangeRateComparison> rateRecords) {
        XYChart chart = new XYChartBuilder().width(640).height(480)
                .title("Exchange rate comparison").xAxisTitle("Time")
                .yAxisTitle("Exchange rate EUR/USD").theme(ChartTheme.Matlab).build();

        chart.getStyler().setSeriesColors(providerColors.values().toArray(new Color[0]));
        chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

        int size = rateRecords.size();
        List<Date> dateTimes = new ArrayList<>(size);

        List<String> providerNames = rateRecords.get(0).providerRates().keySet().stream().toList();

        Map<String, List<Double>> seriesData = new LinkedHashMap<>();
        seriesData.put("Open rate", new ArrayList<>(size));
        for (String provider : providerNames) {
            seriesData.put(provider + " rate", new ArrayList<>(size));
        }

        for (var rateRecord : rateRecords) {
            dateTimes.add(Date.from(rateRecord.timestamp()));
            seriesData.get("Open rate").add(Double.parseDouble(rateRecord.openRate()));

            for (String provider : providerNames) {
                String rate = rateRecord.providerRates().get(provider);
                seriesData.get(provider + " rate")
                        .add(rate != null ? Double.parseDouble(rate) : null);
            }
        }

        for (var entry : seriesData.entrySet()) {
            chart.addSeries(entry.getKey(), dateTimes, entry.getValue());
        }

        return chart;
    }

    private void saveChartAsImageFile(XYChart chart) {
        try {
            BitmapEncoder.saveBitmap(chart, tempImageFilePath, BitmapFormat.PNG);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
