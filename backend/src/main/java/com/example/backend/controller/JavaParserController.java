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
public class JavaParserController {

    private final JavaCodeParser javaCodeParser;

    public JavaParserController(JavaCodeParser javaCodeParser) {
        this.javaCodeParser = javaCodeParser;
    }

    @GetMapping("/{id}/java-analysis")
    public ResponseEntity<?> analyzeJavaFiles(@PathVariable Long id) {

        try {

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

            List<JavaCodeParser.JavaFileAnalysis> results =
                    new ArrayList<>();

            try (var paths = Files.walk(projectDirectory)) {

                paths.filter(Files::isRegularFile)
                        .filter(path ->
                                path.toString()
                                        .toLowerCase()
                                        .endsWith(".java"))
                        .forEach(path -> {

                            try {

                                JavaCodeParser.JavaFileAnalysis analysis =
                                        javaCodeParser.parseJavaFile(path);

                                results.add(analysis);

                            } catch (Exception e) {

                                System.out.println(
                                        "Failed to parse: "
                                                + path.getFileName()
                                                + " - "
                                                + e.getMessage()
                                );
                            }
                        });
            }

            return ResponseEntity.ok(results);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(
                            "Java analysis failed: "
                                    + e.getMessage()
                    );
        }
    }
}