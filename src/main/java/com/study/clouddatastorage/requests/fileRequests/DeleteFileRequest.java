package com.study.clouddatastorage.requests.fileRequests;

import lombok.Data;

@Data
public class DeleteFileRequest {
    private String fileName;
    private Long folderId;
}
