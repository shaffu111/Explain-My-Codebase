package com.example.backend.controller;

import com.example.backend.upload.ProjectFileScanner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectAnalysisController {

    private final ProjectFileScanner projectFileScanner;

    public ProjectAnalysisController(
            ProjectFileScanner projectFileScanner) {

        this.projectFileScanner = projectFileScanner;
    }

    @GetMapping("/{id}/analysis")
    public ResponseEntity<?> analyzeProject(
            @PathVariable Long id) {

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

            Map<String, Object> analysis =
                    projectFileScanner.analyzeProject(
                            projectDirectory
                    );

            return ResponseEntity.ok(analysis);

        } catch (IOException e) {

            return ResponseEntity.internalServerError()
                    .body("Failed to analyze project: "
                            + e.getMessage());
        }
    }
}