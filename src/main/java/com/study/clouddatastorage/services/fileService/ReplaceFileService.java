package com.study.clouddatastorage.services.fileService;

import com.study.clouddatastorage.models.CloudFile;
import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.CloudFileRepository;
import com.study.clouddatastorage.repository.FolderRepository;
import com.study.clouddatastorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
public class ReplaceFileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private CloudFileRepository cloudFileRepository;

    public void replaceFile(String fileName, Long currentFolderId, Long replacingFolderId, String userEmail) throws AccessDeniedException {

        if(Objects.equals(currentFolderId, replacingFolderId)) return;

        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User does not exists"));

        Folder replacingFolder = null;
        if(replacingFolderId != null){
                replacingFolder = folderRepository.findFolderById(replacingFolderId).orElseThrow(() -> new IllegalArgumentException("Replacing folder does not exists"));
        }

        Folder currentFolder = null;
        if(currentFolderId != null){
            currentFolder = folderRepository.findFolderById(currentFolderId).orElseThrow(() -> new IllegalArgumentException("Current folder does not exists"));
        }

        if(cloudFileRepository.existsByNameAndFolder(fileName, replacingFolder)) throw new IllegalArgumentException("File with that name already exists in replacing folder");

        CloudFile file = cloudFileRepository.findByNameAndFolder(fileName, currentFolder).orElseThrow(() -> new IllegalArgumentException("File with that name does not exist"));

        if(!file.getUser().getId().equals(user.getId())) throw new AccessDeniedException("Access denied");

        file.setFolder(replacingFolder);
        cloudFileRepository.save(file);
    }
}
