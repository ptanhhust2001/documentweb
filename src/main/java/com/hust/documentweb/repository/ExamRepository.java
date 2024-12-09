package com.hust.documentweb.repository;


import com.hust.documentweb.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findAll(Specification<Object> spec);
    Page<Exam> findAll(Specification<Object> spec, Pageable pageable);
}
