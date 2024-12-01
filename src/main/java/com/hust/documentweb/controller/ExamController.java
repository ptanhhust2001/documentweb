package com.hust.documentweb.controller;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.exam.ExamReqDTO;
import com.hust.documentweb.dto.exam.ExamReqOpenAiDTO;
import com.hust.documentweb.dto.exam.ExamResDTO;
import com.hust.documentweb.dto.exam.ExamUpdateDTO;
import com.hust.documentweb.service.exam.IExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {
    IExamService service;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ExamResDTO>>> getAll(@RequestParam(required = false) String advanceSearch) {
        return ResponseEntity.ok(ResponseDTO.success(service.findAll(advanceSearch)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ExamResDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDTO.success(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ExamResDTO>> create(@RequestBody @Valid ExamReqDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.create(dto)));
    }

/*    @PostMapping(value = "/questions", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> createExam(@RequestPart("file") MultipartFile file, @RequestBody @Valid ExamReqDTO dto) {
        service.upload(file, dto);
        return ResponseEntity.ok(ResponseDTO.success());
    }*/
    @PostMapping(value = "/questions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @RequestParam Long classId, Long subjectId) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        service.upload(file, classId, subjectId);
        return ResponseEntity.ok(ResponseDTO.success());
    }

    @PostMapping("/generate")
    public ResponseEntity<ResponseDTO<ExamResDTO>> createByGemini(@RequestBody @Valid ExamReqOpenAiDTO dto) throws JsonProcessingException {
        return ResponseEntity.ok(ResponseDTO.success(service.createQuestionByOpenAi(dto)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<ExamResDTO>> update(@RequestParam Long id, @RequestBody @Valid ExamUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.success(service.update(id, dto)));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Void>> deleteAllById(@RequestParam List<Long> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.ok(ResponseDTO.success());
    }

}
