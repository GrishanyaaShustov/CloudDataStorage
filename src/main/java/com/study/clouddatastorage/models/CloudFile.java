package com.study.clouddatastorage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CloudFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, name = "s3_key")
    private String s3Key;

    @Column(nullable = false)
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(nullable = false, name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // связь с пользователем (каждый файл принадлежит пользователю)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // связь с папкой (null — если это корневая папка)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;
}
