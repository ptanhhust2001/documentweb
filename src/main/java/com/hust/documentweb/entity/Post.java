package com.hust.documentweb.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;

import com.hust.documentweb.constant.enums.EPostType;
import com.hust.documentweb.constant.enums.EType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "post")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Column(columnDefinition = "LONGTEXT")
    String content;

    String description;
    String author;
    String imageFilePath;

    @Enumerated(EnumType.STRING)
    EType type;

    @Enumerated(EnumType.STRING)
    EPostType postType;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Material> materials;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    String createBy;

    String updateBy;

    Integer views = 0;
}
