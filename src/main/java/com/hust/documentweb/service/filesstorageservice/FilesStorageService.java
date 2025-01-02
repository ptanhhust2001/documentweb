package com.hust.documentweb.service.filesstorageservice;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hust.documentweb.configuration.ApplicationConfig;
import com.hust.documentweb.constant.ECommon;
import com.hust.documentweb.exception.BookException;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Service
@RequiredArgsConstructor
public class FilesStorageService {
    private ApplicationConfig appConfig;

    private Path root = null;

    public Path getRoot() {
        if (root == null) {
            this.root = Paths.get(appConfig.getDataStoragePath());
            try {
                if (!Files.exists(root)) {
                    Files.createDirectory(root);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize folder for upload!");
            }
        }
        return root;
    }

    public boolean save(MultipartFile file, String folderName) {
        return save(file, folderName, null);
    }

    public boolean save(MultipartFile file, String folderName, String fileName) {
        try {
            Path folderPath = getRoot().resolve(folderName);

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            Path destFile = folderPath.resolve(fileName != null ? fileName : file.getOriginalFilename());
            Files.copy(file.getInputStream(), destFile, StandardCopyOption.REPLACE_EXISTING);

            return Files.exists(destFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public boolean copy(String sourceFolder, String sourceFile, String destFolder, String destFile) {
        try {
            Path sourcePath = getRoot().resolve(sourceFolder).resolve(sourceFile);
            if (!Files.exists(sourcePath.toAbsolutePath())) {
                return false;
            }
            Path destPath = getRoot().resolve(destFolder).resolve(destFile);
            Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            return Files.exists(destPath.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public File get(String filename, String folderName) {
        Path folderPath = getRoot().resolve(folderName);
        Path file = folderPath.resolve(filename);
        if (Files.exists(file)) return file.toFile();
        return null;
    }

    public Resource load(String filename, String folderName) {
        try {
            Path folderPath = getRoot().resolve(folderName);
            Path file = folderPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public File createFile(Path folderPath, String fileName) {
        File file;
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            file = folderPath.resolve(fileName).toFile();
            boolean newFile = file.createNewFile();
            if (newFile) return file;
            else
                throw new BookException(
                        ECommon.SYSTEM_ERROR, Map.of(ECommon.CANNOT_CREATE_FILE, List.of(file.getName())));
        } catch (IOException e) {
            throw new BookException(ECommon.SYSTEM_ERROR, Map.of(ECommon.CANNOT_CREATE_FILE, List.of()));
        }
    }

    public void deleteFile(String filename, String folderName) {
        try {
            Path file = getRoot().resolve(folderName).resolve(filename);
            FileSystemUtils.deleteRecursively(file.toFile());
        } catch (Exception e) {
            // throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteDirectory(Path path) {
        try {
            Path folderPath = getRoot().resolve(path);
            FileSystemUtils.deleteRecursively(folderPath.toFile());
        } catch (Exception ignored) {
        }
    }

    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(getRoot().toFile());
        } catch (Exception ignored) {
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(getRoot(), 1)
                    .filter(path -> !path.equals(getRoot()))
                    .map(getRoot()::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
