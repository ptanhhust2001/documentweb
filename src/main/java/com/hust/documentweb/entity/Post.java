package com.hust.documentweb.entity;

import com.hust.documentweb.constant.enums.EPostType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

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
    EPostType type;

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

    @OneToMany(mappedBy = "post")
    Set<Material> materials;

    LocalDateTime createAt;

    LocalDateTime updateAt;

    String createBy;

    String updateBy;
}
