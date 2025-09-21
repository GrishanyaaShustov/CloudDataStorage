package com.study.clouddatastorage.repository;

import com.study.clouddatastorage.models.CloudFile;
import com.study.clouddatastorage.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CloudFileRepository extends JpaRepository<CloudFile, Long> {
    boolean existsByNameAndFolder(String name, Folder folder);
    Optional<CloudFile> findByNameAndFolder(String name, Folder folder);
}
