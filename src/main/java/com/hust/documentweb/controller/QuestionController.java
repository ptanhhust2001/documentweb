package com.hust.documentweb.controller;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.question.QuestionReqDTO;
import com.hust.documentweb.dto.question.QuestionResDTO;
import com.hust.documentweb.dto.question.QuestionUpdateDTO;
import com.hust.documentweb.service.question.IQuestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    IQuestionService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<QuestionResDTO>>> getAll(@RequestParam(required = false) String advanceSearch) {
        return ResponseEntity.ok(ResponseDTO.success(service.findAll(advanceSearch)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<QuestionResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<QuestionResDTO>> create(@RequestBody @Valid QuestionReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<QuestionResDTO>> update(@RequestParam Long id, @RequestBody @Valid QuestionUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> deleteAllById(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok(ResponseDTO.success());
    }
}
