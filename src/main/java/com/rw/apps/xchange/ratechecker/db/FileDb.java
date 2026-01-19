package com.rw.apps.xchange.ratechecker.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rw.apps.xchange.ratechecker.util.FileHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileDb {
    private static final String DB_FILENAME = "file.db";
    private static final int OLD_LIMIT = 200;
    private final Path dbFilePath;
    private final ObjectMapper objectMapper;

    public FileDb(FileHelper fileHelper, ObjectMapper objectMapper) {
        this.dbFilePath = fileHelper.resolve(DB_FILENAME);
        this.objectMapper = objectMapper;
    }

    public List<ExchangeRateComparison> readAll() throws IOException {
        try (Stream<String> stream = Files.lines(dbFilePath)) {
            return stream.map(this::parseLine).toList();
        }
    }

    public ExchangeRateComparison getLastRecord() throws IOException {
        if (!Files.exists(dbFilePath)) {
            return null;
        }
        try (Stream<String> lines = Files.lines(dbFilePath)) {
            return lines.reduce((first, second) -> second)
                    .map(this::parseLine)
                    .orElse(null);
        }
    }

    public void persistRecord(ExchangeRateComparison rateRecord) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(dbFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.append(objectMapper.writeValueAsString(rateRecord)).append('\n');
            writer.flush();
        }
    }

    public void clearOldRecords() throws IOException {
        List<String> lines = Files.readAllLines(dbFilePath);
        if (lines.size() > OLD_LIMIT) {
            Files.write(dbFilePath, lines.stream().skip((long) lines.size() - OLD_LIMIT).toList());
        }
    }

    public void clear() throws IOException {
        try (InputStream ignored = Files.newInputStream(dbFilePath, StandardOpenOption.TRUNCATE_EXISTING)) {
            log.info("File cleared.");
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
