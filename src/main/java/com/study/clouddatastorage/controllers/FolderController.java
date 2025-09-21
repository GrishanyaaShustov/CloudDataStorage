package com.study.clouddatastorage.controllers;

import com.study.clouddatastorage.repository.FolderRepository;
import com.study.clouddatastorage.repository.UserRepository;
import com.study.clouddatastorage.requests.folderRequests.CreateFolderRequest;
import com.study.clouddatastorage.requests.folderRequests.RenameFolderRequest;
import com.study.clouddatastorage.services.folderServices.CreateFolderService;
import com.study.clouddatastorage.services.folderServices.RefactorFoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@RestController
@RequestMapping("/api/folder")
public class FolderController {

    @Autowired
    CreateFolderService createFolderService;

    @Autowired
    RefactorFoldersService refactorFoldersService;

    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody CreateFolderRequest request, Principal principal) {
        try{
            createFolderService.createFolder(request, principal.getName());
            return ResponseEntity.ok("Folder created successfully");
        }
        catch (IllegalArgumentException | AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long folderId, Principal principal){
        try{
            refactorFoldersService.deleteFolder(folderId, principal.getName());
            return ResponseEntity.ok("Folder deleted successfully");
        } catch (IllegalArgumentException | AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/rename")
    public ResponseEntity<?> renameFolder(@RequestBody RenameFolderRequest request, Principal principal){
        try {
            refactorFoldersService.renameFolder(request.getFolderId(), request.getFolderNewName(), principal.getName());
            return ResponseEntity.ok("Folder renamed successfully");
        } catch (IllegalArgumentException | AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
