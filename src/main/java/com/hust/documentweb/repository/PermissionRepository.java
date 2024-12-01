package com.hust.documentweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.documentweb.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
