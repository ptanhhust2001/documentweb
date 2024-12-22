package com.hust.documentweb.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.comment.CommentReqDTO;
import com.hust.documentweb.dto.comment.CommentResDTO;
import com.hust.documentweb.dto.comment.CommentUpdateDTO;
import com.hust.documentweb.service.comment.ICommentService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    ICommentService service;

    @GetMapping
    public ResponseEntity<ResponsePageDTO<List<CommentResDTO>>> getAll(
            @RequestParam(required = false) String advanceSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findAll(advanceSearch, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CommentResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CommentResDTO>> create(@RequestBody CommentReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<CommentResDTO>> update(@PathVariable Long id, @RequestBody CommentUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }
}
