package com.example.backend.controller;

import com.example.backend.parser.JavaCodeParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    private final JavaCodeParser javaCodeParser;

    public SearchController(JavaCodeParser javaCodeParser) {
        this.javaCodeParser = javaCodeParser;
    }

    @GetMapping("/{id}/search")
    public ResponseEntity<?> search(
            @PathVariable Long id,
            @RequestParam String query) {

        Path projectDirectory = Paths.get(
                System.getProperty("user.dir"),
                "uploads",
                "projects",
                id.toString(),
                "extracted"
        );

        if (!Files.exists(projectDirectory)) {
            return ResponseEntity.notFound().build();
        }

        List<String> results = new ArrayList<>();

        try (var paths = Files.walk(projectDirectory)) {

            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString()
                            .toLowerCase()
                            .endsWith(".java"))
                    .forEach(path -> {

                        try {
                            JavaCodeParser.JavaFileAnalysis analysis =
                                    javaCodeParser.parseJavaFile(path);

                            analysis.getClasses().forEach(classAnalysis -> {

                                if (classAnalysis.getName()
                                        .toLowerCase()
                                        .contains(query.toLowerCase())) {

                                    results.add(
                                            "Class: "
                                                    + classAnalysis.getName()
                                                    + " | File: "
                                                    + path.getFileName()
                                    );
                                }

                                classAnalysis.getMethods()
                                        .forEach(method -> {

                                            if (method.getName()
                                                    .toLowerCase()
                                                    .contains(query.toLowerCase())) {

                                                results.add(
                                                        "Method: "
                                                                + method.getName()
                                                                + " | Class: "
                                                                + classAnalysis.getName()
                                                                + " | File: "
                                                                + path.getFileName()
                                                );
                                            }
                                        });
                            });

                        } catch (Exception ignored) {
                        }
                    });

            return ResponseEntity.ok(results);

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Search failed: " + e.getMessage());
        }
    }
}