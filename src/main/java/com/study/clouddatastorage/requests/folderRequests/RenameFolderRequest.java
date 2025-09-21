package com.study.clouddatastorage.requests.folderRequests;

import lombok.Data;

@Data
public class RenameFolderRequest {
    private Long folderId;
    private String folderNewName;
}
