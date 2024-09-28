package com.rw.apps.xchange.ratechecker.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileHelper {
    private final Path dataDir;

    @SneakyThrows(IOException.class)
    public FileHelper(@Value("${data-dir:temp}") String dataDir) {
        this.dataDir = Path.of(dataDir);
        Files.createDirectories(this.dataDir);
    }

    public Path resolve(String path) {
        return dataDir.resolve(path);
    }
}
