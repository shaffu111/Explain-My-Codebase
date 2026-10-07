package com.example.backend.controller;

import com.example.backend.parser.JavaCodeParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
public class DependencyController {

    private final JavaCodeParser javaCodeParser;

    public DependencyController(JavaCodeParser javaCodeParser) {
        this.javaCodeParser = javaCodeParser;
    }

    @GetMapping("/{id}/dependencies")
    public ResponseEntity<?> getDependencies(@PathVariable Long id) {

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

        Map<String, List<String>> dependencies = new LinkedHashMap<>();

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

                            dependencies.put(
                                    path.getFileName().toString(),
                                    analysis.getImports()
                            );

                        } catch (Exception e) {

                            dependencies.put(
                                    path.getFileName().toString(),
                                    List.of()
                            );
                        }
                    });

            return ResponseEntity.ok(dependencies);

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Dependency analysis failed: "
                            + e.getMessage());
        }
    }
}