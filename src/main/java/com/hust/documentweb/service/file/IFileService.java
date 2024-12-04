package com.hust.documentweb.service.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String save(MultipartFile file);

    Resource load(String filename);
}
