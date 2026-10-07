package com.example.backend.upload;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ProjectFileScanner {

    public Map<String, Object> analyzeProject(Path projectDirectory)
            throws IOException {

        int totalFiles = 0;
        int javaFiles = 0;
        int javascriptFiles = 0;
        int htmlFiles = 0;
        int cssFiles = 0;
        int jsonFiles = 0;
        int imageFiles = 0;
        int otherFiles = 0;

        try (Stream<Path> paths = Files.walk(projectDirectory)) {

            for (Path path : (Iterable<Path>) paths::iterator) {

                if (!Files.isRegularFile(path)) {
                    continue;
                }

                totalFiles++;

                String fileName =
                        path.getFileName().toString().toLowerCase();

                if (fileName.endsWith(".java")) {

                    javaFiles++;

                } else if (fileName.endsWith(".js")
                        || fileName.endsWith(".jsx")
                        || fileName.endsWith(".ts")
                        || fileName.endsWith(".tsx")) {

                    javascriptFiles++;

                } else if (fileName.endsWith(".html")
                        || fileName.endsWith(".htm")) {

                    htmlFiles++;

                } else if (fileName.endsWith(".css")
                        || fileName.endsWith(".scss")) {

                    cssFiles++;

                } else if (fileName.endsWith(".json")) {

                    jsonFiles++;

                } else if (fileName.endsWith(".jpg")
                        || fileName.endsWith(".jpeg")
                        || fileName.endsWith(".png")
                        || fileName.endsWith(".gif")
                        || fileName.endsWith(".svg")
                        || fileName.endsWith(".webp")) {

                    imageFiles++;

                } else {

                    otherFiles++;
                }
            }
        }

        String projectType = determineProjectType(
                javaFiles,
                javascriptFiles,
                htmlFiles,
                cssFiles
        );

        Map<String, Object> result = new HashMap<>();

        result.put("totalFiles", totalFiles);
        result.put("javaFiles", javaFiles);
        result.put("javascriptFiles", javascriptFiles);
        result.put("htmlFiles", htmlFiles);
        result.put("cssFiles", cssFiles);
        result.put("jsonFiles", jsonFiles);
        result.put("imageFiles", imageFiles);
        result.put("otherFiles", otherFiles);
        result.put("projectType", projectType);

        return result;
    }

    private String determineProjectType(
            int javaFiles,
            int javascriptFiles,
            int htmlFiles,
            int cssFiles) {

        if (javaFiles > 0 && javascriptFiles > 0) {
            return "Java + JavaScript";
        }

        if (javaFiles > 0) {
            return "Java";
        }

        if (javascriptFiles > 0) {
            return "JavaScript";
        }

        if (htmlFiles > 0 || cssFiles > 0) {
            return "Web Project";
        }

        return "Unknown";
    }
}