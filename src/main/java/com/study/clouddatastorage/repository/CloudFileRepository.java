package com.study.clouddatastorage.repository;

import com.study.clouddatastorage.models.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CloudFileRepository extends JpaRepository<CloudFile, Long> {

}
