package com.hust.documentweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.documentweb.entity.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {}
