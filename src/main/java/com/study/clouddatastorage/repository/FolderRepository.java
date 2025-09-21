package com.study.clouddatastorage.repository;

import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    boolean existsFolderByNameAndParentAndUser(String name, Folder parent, User user);
    boolean existsById(Long id);
    Optional<Folder> findFolderById(Long id);
}
