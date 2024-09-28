package com.rw.apps.xchange.ratechecker.util;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileHelperTest {
    private static final String TEMP = "/temp";
    private final FileHelper fileHelper = new FileHelper(TEMP);

    @Test
    void resolvesFilePathCorrectly() {
        Path resolvedPath = fileHelper.resolve("data.txt");
        assertEquals("/temp/data.txt", resolvedPath.toString().replace("\\", "/"));
    }
}
