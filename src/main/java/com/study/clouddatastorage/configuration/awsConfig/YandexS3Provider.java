package com.study.clouddatastorage.configuration.awsConfig;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class YandexS3Provider {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private YandexS3Config config;
}
