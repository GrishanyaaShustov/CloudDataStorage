package com.study.clouddatastorage.requests.fileRequests;

import lombok.Data;

@Data
public class ReplaceFileRequest {
    private String fileName;
    private Long currentFolderId;
    private Long newFolderId;
}
