package com.service.microservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        Path uploadPath = Paths.get(uploadDir, subDirectory).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path filePath = uploadPath.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return subDirectory + "/" + uniqueFileName;
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            Path filePath = Paths.get(uploadDir, fileUrl).toAbsolutePath().normalize();
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Gérer l'exception (log, etc.)
            }
        }
    }

}
