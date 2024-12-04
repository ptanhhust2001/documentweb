package com.hust.documentweb.controller;

import com.hust.documentweb.service.file.IFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
    IFileService fileService;
    @GetMapping("/images/{id}")
    public Resource getImage(@PathVariable("id") String imageUri) {
        return fileService.load(imageUri);
    }
}
