package com.openclassrooms.chatop.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service for handling file storage operations.
 * Manages image uploads for rental properties.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Store a file and return its URL.
     *
     * @param file the file to store
     * @return the URL to access the file
     * @throws RuntimeException if file storage fails
     */
    public String storeFile(MultipartFile file) {
        // Validate file
        validateFile(file);

        // Generate unique filename
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID() + "." + fileExtension;

        try {
            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Build and return the file URL
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(newFilename)
                    .toUriString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + newFilename + ". Please try again!", ex);
        }
    }

    /**
     * Validate the uploaded file.
     *
     * @param file the file to validate
     * @throws RuntimeException if validation fails
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty. Please select a file to upload.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds maximum allowed size of 10MB.");
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (filename.contains("..")) {
            throw new RuntimeException("Invalid file path: " + filename);
        }

        String extension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new RuntimeException("File type not allowed. Only images are accepted (jpg, jpeg, png, gif, webp).");
        }
    }

    /**
     * Get file extension from filename.
     *
     * @param filename the filename
     * @return the file extension
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}
