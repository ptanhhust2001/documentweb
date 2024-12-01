package com.hust.documentweb.controller;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.subject.SubjectReqDTO;
import com.hust.documentweb.dto.subject.SubjectResDTO;
import com.hust.documentweb.service.subject.ISubjectService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/subjects")
@AllArgsConstructor
public class SubjectController {
    ISubjectService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubjectResDTO>>> getAll() {
        return ResponseEntity.ok(ResponseDTO.success(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<SubjectResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<SubjectResDTO>> create(@RequestBody SubjectReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<SubjectResDTO>> update(@RequestParam Long id,@RequestBody SubjectReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Long> ids) {

        return ResponseEntity.ok().build();
    }
}