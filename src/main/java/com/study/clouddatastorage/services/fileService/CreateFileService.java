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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Service
public class CreateFileService {

    @Autowired
    private YandexS3Provider provider;

    @Autowired
    private CloudFileRepository cloudFileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;


    public void uploadFile(MultipartFile file, String userEmail, Long folderId)
            throws IOException, AccessDeniedException, IllegalArgumentException {

        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exists"));

        Folder folder = null;
        if (folderId != null) {
            folder = folderRepository.findFolderById(folderId)
                    .orElseThrow(() -> new IllegalArgumentException("Folder does not exists"));

            if (!user.getId().equals(folder.getUser().getId())) {
                throw new AccessDeniedException("Access denied");
            }
        }

        // проверка уникальности имени файла в папке (или в корне)
        if (cloudFileRepository.existsByNameAndFolder(file.getOriginalFilename(), folder)) {
            throw new IllegalArgumentException("File already exists");
        }

        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        provider.uploadFile(key, file.getInputStream(), file.getSize(), file.getContentType());

        CloudFile cloudFile = new CloudFile();
        cloudFile.setName(file.getOriginalFilename());
        cloudFile.setS3Key(key);
        cloudFile.setContentType(file.getContentType());
        cloudFile.setSize(file.getSize());
        cloudFile.setUser(user);
        cloudFile.setFolder(folder); // null = корневая папка

        cloudFileRepository.save(cloudFile);
    }

    public void copyFile(String fileName, Long folderId, Long copyFolderId, String userEmail) throws AccessDeniedException, IOException {
        if (folderId != null && folderId.equals(copyFolderId))
            throw new IllegalArgumentException("Cannot copy file in the same folder");

        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Folder folder = null;
        if (folderId != null) {
            folder = folderRepository.findFolderById(folderId)
                    .orElseThrow(() -> new IllegalArgumentException("Folder does not exist"));
        }

        Folder copyFolder = null;
        if (copyFolderId != null) {
            copyFolder = folderRepository.findFolderById(copyFolderId)
                    .orElseThrow(() -> new IllegalArgumentException("Copy folder does not exist"));
        }

        if (cloudFileRepository.existsByNameAndFolder(fileName, copyFolder))
            throw new IllegalArgumentException("File with that name already exists in the destination folder");

        CloudFile file = cloudFileRepository.findByNameAndFolder(fileName, folder)
                .orElseThrow(() -> new IllegalArgumentException("File with that name does not exist"));

        if (!file.getUser().getId().equals(user.getId()))
            throw new AccessDeniedException("Access denied");

        // Генерация нового ключа для копии
        String newKey = UUID.randomUUID() + "_" + file.getName();

        // Копирование файла в S3
        try {
            provider.copyFile(file.getS3Key(), newKey);
        } catch (Exception e) {
            throw new IOException("Failed to copy file in S3", e);
        }

        // Создаем новый объект в базе
        CloudFile copiedFile = new CloudFile();
        copiedFile.setName(file.getName());
        copiedFile.setS3Key(newKey);
        copiedFile.setContentType(file.getContentType());
        copiedFile.setSize(file.getSize());
        copiedFile.setUser(user);
        copiedFile.setFolder(copyFolder);

        cloudFileRepository.save(copiedFile);
    }
}
