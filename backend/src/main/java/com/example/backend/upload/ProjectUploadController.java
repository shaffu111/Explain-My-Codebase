package com.example.backend.upload;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectUploadController {

    private final ProjectUploadService projectUploadService;
    private final ZipExtractionService zipExtractionService;

    public ProjectUploadController(
            ProjectUploadService projectUploadService,
            ZipExtractionService zipExtractionService) {

        this.projectUploadService = projectUploadService;
        this.zipExtractionService = zipExtractionService;
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadProject(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("File is empty");
            }

            // Save ZIP
            String savedPath =
                    projectUploadService.saveFile(id, file);

            // Convert saved path into Path
            Path zipPath = Paths.get(savedPath);

            // Extraction directory
            Path extractionDirectory =
                    zipPath.getParent().resolve("extracted");

            // Extract ZIP
            zipExtractionService.extractZip(
                    zipPath,
                    extractionDirectory
            );

            return ResponseEntity.ok(
                    "File uploaded and extracted successfully. " +
                    "Location: " + extractionDirectory
            );
} catch (Exception e) {
    e.printStackTrace();

    return ResponseEntity.internalServerError()
            .body(
                "Failed to process file: "
                + e.getClass().getName()
                + " - "
                + e.getMessage()
            );
}

    }
}