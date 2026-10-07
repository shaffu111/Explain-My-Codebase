package com.example.backend.upload;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProjectUploadService {

    private final Path uploadDirectory =
            Paths.get(System.getProperty("user.dir"), "uploads", "projects");

    public String saveFile(Long projectId, MultipartFile file) throws IOException {

        // Create project-specific directory
        Path projectDirectory = uploadDirectory.resolve(projectId.toString());

        Files.createDirectories(projectDirectory);

        // Get only the filename
        String fileName = Paths.get(
                file.getOriginalFilename()
        ).getFileName().toString();

        // Final file location
        Path destination = projectDirectory.resolve(fileName);

        // Save file
        file.transferTo(destination.toFile());

        return destination.toAbsolutePath().toString();
    }
}