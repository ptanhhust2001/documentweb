package com.hust.documentweb.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "class")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToMany
    List<Subject> subjects;

    @OneToMany(mappedBy = "classEntity")
    Set<Post> posts;

    @OneToMany(mappedBy = "classEntity")
    Set<Exam> exams;
}
