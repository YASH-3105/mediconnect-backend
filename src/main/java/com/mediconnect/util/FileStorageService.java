package com.mediconnect.util;

import com.mediconnect.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final java.util.List<String> ALLOWED_TYPES = java.util.List.of(
            "image/jpeg", "image/png", "application/pdf"
    );

    public String storeFile(MultipartFile file, Long patientId) {
        validateFile(file);

        try {
            Path uploadPath = Paths.get(uploadDir, String.valueOf(patientId));
            Files.createDirectories(uploadPath);

            String uniqueFileName = UUID.randomUUID() + "_" 
                    + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, 
                    StandardCopyOption.REPLACE_EXISTING);

            log.info("File stored: {}", filePath);
            return filePath.toString();
        } catch (IOException ex) {
            log.error("File storage error: {}", ex.getMessage());
            throw new BadRequestException("Failed to store file. Please try again.");
        }
    }

    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
            log.info("File deleted: {}", filePath);
        } catch (IOException ex) {
            log.error("Failed to delete file: {}", filePath);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Please provide a valid file");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds 10MB limit.");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException(
                    "Invalid file type. Only JPG, PNG, and PDF are allowed.");
        }
    }
}