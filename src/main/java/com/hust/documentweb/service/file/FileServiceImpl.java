package com.hust.documentweb.service.file;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.exception.BookException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements IFileService {
    Path root = Paths.get("./uploads");

    @NonFinal
    @Value("${file.path}")
    String linkPath;

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException ex) {
            throw new BookException(FunctionError.CREATE_FAILED, "Could not initialize root folder");
        }
    }

    @Override
    public String save(MultipartFile file) {

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
            return linkPath + fileName;
        } catch (Exception ex) {
            throw new BookException(FunctionError.UPLOAD_FAILED, ex.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        if (filename == null) return null;
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BookException(FunctionError.NOT_FOUND, "Could not read the file!");
            }
        } catch (MalformedURLException mex) {
            throw new BookException(FunctionError.NOT_FOUND, "Error: " + mex.getMessage());
        }
    }
}
