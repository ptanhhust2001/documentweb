package com.hust.documentweb.controller;

import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.exam.*;
import com.hust.documentweb.service.exam.IExamService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {
    IExamService service;

    @GetMapping
    public ResponseEntity<ResponsePageDTO<List<ExamResDTO>>> getAll(
            @RequestParam(required = false) String advanceSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findAll(advanceSearch, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ExamExResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ExamResDTO>> create(@RequestBody @Valid ExamReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

    @PostMapping("/check-answer")
    public ResponseEntity<ResponseDTO<ExamResResultDTO>> checkAnswer(@RequestBody ExamReqResultDTO answer) {
        return ResponseEntity.ok(ResponseDTO.success(service.checkAnswer(answer)));
    }

    /*    @PostMapping(value = "/questions", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> createExam(@RequestPart("file") MultipartFile file, @RequestBody @Valid ExamReqDTO dto) {
    	service.upload(file, dto);
    	return ResponseEntity.ok(ResponseDTO.success());
    }*/
    @PostMapping(value = "/questions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @RequestParam Long classId, Long subjectId)
            throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        service.upload(file, classId, subjectId);
        return ResponseEntity.ok(ResponseDTO.success());
    }

    @PostMapping("/generate")
    public ResponseEntity<ResponseDTO<ExamResDTO>> createByGemini(@RequestBody @Valid ExamReqOpenAiDTO dto)
            throws JsonProcessingException {
        return ResponseEntity.ok(ResponseDTO.success(service.createQuestionByOpenAi(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<ExamResDTO>> update(
            @RequestParam Long id, @RequestBody @Valid ExamUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> deleteAllById(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok(ResponseDTO.success());
    }
}
