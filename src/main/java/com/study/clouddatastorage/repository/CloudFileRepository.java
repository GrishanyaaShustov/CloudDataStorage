package com.study.clouddatastorage.repository;

import com.study.clouddatastorage.models.CloudFile;
import com.study.clouddatastorage.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CloudFileRepository extends JpaRepository<CloudFile, Long> {
    boolean existsByNameAndFolder(String name, Folder folder);
}
