package com.study.clouddatastorage.services.fileService;

import com.study.clouddatastorage.configuration.awsConfig.YandexS3Provider;
import com.study.clouddatastorage.models.CloudFile;
import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.CloudFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadFileService {

    @Autowired
    private YandexS3Provider provider;

    @Autowired
    private CloudFileRepository cloudFileRepository;

    public CloudFile uploadFile(MultipartFile file, User user, Folder folder) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        provider.uploadFile(key, file.getInputStream(), file.getSize(), file.getContentType());

        CloudFile cloudFile = new CloudFile();
        cloudFile.setName(file.getOriginalFilename());
        cloudFile.setS3Key(key);
        cloudFile.setContentType(file.getContentType());
        cloudFile.setUser(user);
        cloudFile.setFolder(folder);

        return cloudFileRepository.save(cloudFile);

    }
}
