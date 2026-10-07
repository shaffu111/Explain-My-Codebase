package com.example.backend.upload;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ZipExtractionService {

    public Path extractZip(Path zipFile, Path extractionDirectory) throws IOException {

        Files.createDirectories(extractionDirectory);

        Path basePath = extractionDirectory.toAbsolutePath().normalize();

        try (ZipFile archive = ZipFile.builder()
                .setPath(zipFile)
                .get()) {

            var entries = archive.getEntries();

            while (entries.hasMoreElements()) {

                ZipArchiveEntry entry = entries.nextElement();

                String entryName = entry.getName();

                Path targetPath = basePath
                        .resolve(entryName)
                        .normalize();

                if (!targetPath.startsWith(basePath)) {
                    throw new IOException(
                            "Invalid ZIP entry: " + entryName
                    );
                }

                if (entry.isDirectory()) {

                    Files.createDirectories(targetPath);

                } else {

                    Path parent = targetPath.getParent();

                    if (parent != null) {
                        Files.createDirectories(parent);
                    }

                    try (InputStream inputStream =
                                 archive.getInputStream(entry)) {

                        Files.copy(
                                inputStream,
                                targetPath,
                                StandardCopyOption.REPLACE_EXISTING
                        );
                    }
                }
            }
        }

        return basePath;
    }
}