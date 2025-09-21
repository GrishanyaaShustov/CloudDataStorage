package com.study.clouddatastorage.configuration.awsConfig;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class YandexS3Provider {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private YandexS3Config config;

    public void uploadFile(String key, InputStream inputStream, long size, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(config.getBucket())
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, size));
    }

    public void deleteFile(String key){
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(config.getBucket())
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
