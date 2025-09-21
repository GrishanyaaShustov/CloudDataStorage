package com.study.clouddatastorage.services.folderServices;

import com.study.clouddatastorage.models.Folder;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.FolderRepository;
import com.study.clouddatastorage.repository.UserRepository;
import com.study.clouddatastorage.requests.folderRequests.CreateFolderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class CreateFolderService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    public Folder createFolder(CreateFolderRequest request, String userEmail) throws AccessDeniedException {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        Folder parentFolder = null;
        if(request.getParentId() != null){
            parentFolder = folderRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Cannot create a folder in a non-existent directory"));
            if(!parentFolder.getUser().getId().equals(user.getId())){
                throw new AccessDeniedException("Access denied");
            }
        }

        if(folderRepository.existsFolderByNameAndParentAndUser(request.getFolderName(), parentFolder, user)){
            throw new IllegalArgumentException("Folder with this name already exists in this directory");
        }

        Folder newFolder = new Folder();
        newFolder.setName(request.getFolderName());
        newFolder.setUser(user);
        newFolder.setParent(parentFolder);
        return folderRepository.save(newFolder);
    }

}
