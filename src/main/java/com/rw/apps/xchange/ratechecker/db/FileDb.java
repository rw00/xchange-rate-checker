package com.rw.apps.xchange.ratechecker.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rw.apps.xchange.ratechecker.util.FileHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class FileDb {
    private static final String DB_FILENAME = "file.db";
    private static final int OLD_LIMIT = 200;
    private final Path dbFilePath;
    private final ObjectMapper objectMapper;

    public FileDb(FileHelper fileHelper, ObjectMapper objectMapper) {
        this.dbFilePath = fileHelper.resolve(DB_FILENAME);
        this.objectMapper = objectMapper;
    }

    public void persistRecord(ExchangeRateComparison rateRecord) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(dbFilePath,
                                                             StandardOpenOption.CREATE,
                                                             StandardOpenOption.APPEND)) {
            writer.append(objectMapper.writeValueAsString(rateRecord)).append('\n');
            writer.flush();
        }
    }

    public List<ExchangeRateComparison> readAll() throws IOException {
        try (Stream<String> stream = Files.lines(dbFilePath)) {
            return stream.map(this::parseLine).toList();
        }
    }

    public void clearOldRecords() throws IOException {
        List<String> lines = Files.readAllLines(dbFilePath);
        if (lines.size() > OLD_LIMIT) {
            Files.write(dbFilePath, lines.stream().skip(lines.size() - OLD_LIMIT).toList());
        }
    }

    private ExchangeRateComparison parseLine(String line) {
        try {
            return objectMapper.readValue(line, ExchangeRateComparison.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse line", e);
        }
    }
}
