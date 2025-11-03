package com.openclassrooms.chatop.api.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for file storage operations.
 * Defines the contract for managing image uploads for rental properties.
 */
public interface IFileStorageService {

    /**
     * Store a file and return its URL.
     *
     * @param file the file to store
     * @return the URL to access the file
     * @throws RuntimeException if file storage fails
     */
    String storeFile(MultipartFile file);
}
