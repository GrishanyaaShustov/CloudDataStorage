package com.study.clouddatastorage.requests.fileRequests;

import lombok.Data;

@Data
public class RenameFileRequest {
    private String oldFileName;
    private String newFileName;
    private Long folderId;
}
