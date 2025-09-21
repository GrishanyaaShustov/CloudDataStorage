package com.study.clouddatastorage.services.fileService;

import com.study.clouddatastorage.configuration.awsConfig.YandexS3Provider;
import com.study.clouddatastorage.models.CloudFile;
import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.CloudFileRepository;
import com.study.clouddatastorage.repository.FolderRepository;
import com.study.clouddatastorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class DeleteFileService {

    @Autowired
    private YandexS3Provider provider;

    @Autowired
    private CloudFileRepository cloudFileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    public void deleteFile(String fileName, String userEmail, Long folderId ) throws AccessDeniedException {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Folder folder = null;
        if (folderId != null) {
            folder = folderRepository.findFolderById(folderId)
                    .orElseThrow(() -> new IllegalArgumentException("Folder does not exist"));
        }

        // Найти файл в базе
        CloudFile file = cloudFileRepository.findByNameAndFolder(fileName, folder)
                .orElseThrow(() -> new IllegalArgumentException("File does not exist"));

        // Проверка доступа
        if (!file.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        provider.deleteFile(file.getS3Key());
        cloudFileRepository.delete(file);
    }

}
