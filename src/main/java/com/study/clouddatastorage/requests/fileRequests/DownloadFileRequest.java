package com.study.clouddatastorage.requests.fileRequests;

import lombok.Data;

@Data
public class DownloadFileRequest {
    private String fileName;
    private Long folderId;
}
