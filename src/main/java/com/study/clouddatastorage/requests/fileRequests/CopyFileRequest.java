package com.study.clouddatastorage.requests.fileRequests;

import lombok.Data;

@Data
public class CopyFileRequest {
    private String fileName;
    private Long folderId;
    private Long copyFolderId;
}
