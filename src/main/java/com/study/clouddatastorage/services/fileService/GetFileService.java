package com.study.clouddatastorage.services.fileService;

import com.study.clouddatastorage.configuration.awsConfig.YandexS3Provider;
import com.study.clouddatastorage.models.CloudFile;
import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.CloudFileRepository;
import com.study.clouddatastorage.repository.FolderRepository;
import com.study.clouddatastorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.AccessDeniedException;

@Service
public class GetFileService {

    @Autowired
    private YandexS3Provider provider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CloudFileRepository cloudFileRepository;

    public ResponseEntity<InputStreamResource> downloadFile(String fileName, Long folderId, String userEmail) throws AccessDeniedException {

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

        String key = file.getS3Key();

        InputStream inputStream = provider.downloadFile(key);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType()) // <-- из базы или S3
                .body(resource);
    }
}
