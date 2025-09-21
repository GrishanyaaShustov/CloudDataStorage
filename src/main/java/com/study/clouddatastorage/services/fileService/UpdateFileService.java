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
public class UpdateFileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CloudFileRepository cloudFileRepository;

    public void renameFile(String oldFileName, String newFileName, Long folderId, String userEmail) throws AccessDeniedException {

        if (Objects.equals(oldFileName, newFileName)) return;

        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Folder folder = null;
        if (folderId != null) {
            folder = folderRepository.findFolderById(folderId)
                    .orElseThrow(() -> new IllegalArgumentException("Folder does not exist"));
        }

        // Найти файл в базе
        CloudFile file = cloudFileRepository.findByNameAndFolder(oldFileName, folder)
                .orElseThrow(() -> new IllegalArgumentException("File does not exist"));

        // Проверка доступа
        if (!file.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        // Получаем расширение исходного файла
        String extension = "";
        int dotIndex = oldFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = oldFileName.substring(dotIndex);
        }

        // Если новое имя не содержит расширения, добавляем исходное
        if (!newFileName.contains(".") && !extension.isEmpty()) {
            newFileName += extension;
        }

        // Проверка на существование файла с таким именем в папке
        if (cloudFileRepository.existsByNameAndFolder(newFileName, folder)) {
            throw new IllegalArgumentException("File with this name already exists");
        }

        file.setName(newFileName);
        cloudFileRepository.save(file);
    }
}
