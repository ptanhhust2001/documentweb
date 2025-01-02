package com.hust.documentweb.controller;

import java.util.List;

import com.hust.documentweb.constant.CommonConstrant;
import com.hust.documentweb.constant.enums.EPostType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import com.hust.documentweb.service.post.IPostService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    IPostService service;

    @GetMapping
    public ResponseEntity<ResponsePageDTO<List<PostResDTO>>> getAll(
            @RequestParam(required = false) String advanceSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok((service.findAll(pageable, advanceSearch)));
    }

    @GetMapping("/internal")
    public ResponseEntity<ResponsePageDTO<List<PostResDTO>>> getAllInternal(
            @RequestParam(required = false) String advanceSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        advanceSearch =  (advanceSearch == null || advanceSearch.isBlank() ? "" : advanceSearch + CommonConstrant.AND) + CommonConstrant.POST_TYPE + CommonConstrant.EQUAL + CommonConstrant.INTERNAL;
        return ResponseEntity.ok((service.findAll(pageable, advanceSearch)));
    }

    @GetMapping("/community")
    public ResponseEntity<ResponsePageDTO<List<PostResDTO>>> getAllCommunity(
            @RequestParam(required = false) String advanceSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        advanceSearch = (advanceSearch == null || advanceSearch.isBlank() ? "" : advanceSearch + CommonConstrant.AND) + CommonConstrant.POST_TYPE + CommonConstrant.EQUAL + CommonConstrant.COMMUNITY;
        return ResponseEntity.ok((service.findAll(pageable, advanceSearch)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PostResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<PostResDTO>> create(@RequestBody PostReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto, EPostType.INTERNAL)));
    }

    @PostMapping("/community")
    public ResponseEntity<ResponseDTO<PostResDTO>> createCommunity(@RequestBody PostReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto, EPostType.COMMUNITY)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<PostResDTO>> update(@RequestParam Long id, @RequestBody PostUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }
}
