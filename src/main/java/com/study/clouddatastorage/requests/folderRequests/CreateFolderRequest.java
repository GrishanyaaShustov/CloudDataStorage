package com.study.clouddatastorage.requests.folderRequests;

import lombok.Data;

@Data
public class CreateFolderRequest {
    private String folderName;
    private Long parentId;
}
