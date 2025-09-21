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
}
