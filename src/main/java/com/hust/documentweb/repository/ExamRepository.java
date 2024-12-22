package com.hust.documentweb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.documentweb.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findAll(Specification<Object> spec);

    Page<Exam> findAll(Specification<Object> spec, Pageable pageable);
}
