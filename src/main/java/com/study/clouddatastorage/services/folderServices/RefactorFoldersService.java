package com.study.clouddatastorage.services.folderServices;

import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.FolderRepository;
import com.study.clouddatastorage.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class RefactorFoldersService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    public void deleteFolder(Long folderId, String userEmail) throws AccessDeniedException {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exists"));

        Folder folder = folderRepository.findFolderById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Folder does not exists"));

        if(!folder.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("Access denied");
        }

        folderRepository.delete(folder);
    }

    @Transactional
    public void renameFolder(Long folderId, String newFolderName, String userEmail) throws AccessDeniedException {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exists"));
        Folder folder = folderRepository.findFolderById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Folder does not exists"));
        if(!folder.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("Access denied");
        }

        folder.setName(newFolderName);
    }
}
