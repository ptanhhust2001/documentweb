package com.hust.documentweb.controller;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import com.hust.documentweb.service.post.IPostService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    IPostService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<PostResDTO>>> getAll(@RequestParam(required = false) String advanceSearch,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ResponseDTO.success(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PostResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<PostResDTO>> create(@RequestBody PostReqDTO dto, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto, file)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<PostResDTO>> update(@RequestParam Long id,@RequestBody PostUpdateDTO dto, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id,dto, file)));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }
}