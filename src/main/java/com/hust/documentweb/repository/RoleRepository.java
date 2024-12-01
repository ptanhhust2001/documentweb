package com.hust.documentweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.documentweb.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
