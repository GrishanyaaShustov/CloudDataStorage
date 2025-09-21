package com.study.clouddatastorage.controllers;

import com.study.clouddatastorage.requests.fileRequests.CopyFileRequest;
import com.study.clouddatastorage.requests.fileRequests.DeleteFileRequest;
import com.study.clouddatastorage.requests.fileRequests.RenameFileRequest;
import com.study.clouddatastorage.requests.fileRequests.ReplaceFileRequest;
import com.study.clouddatastorage.services.fileService.CreateFileService;
import com.study.clouddatastorage.services.fileService.DeleteFileService;
import com.study.clouddatastorage.services.fileService.ReplaceFileService;
import com.study.clouddatastorage.services.fileService.UpdateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private CreateFileService createFileService;

    @Autowired
    private DeleteFileService deleteFileService;

    @Autowired
    private ReplaceFileService replaceFileService;

    @Autowired
    private UpdateFileService updateFileService;

    // Realized all CRUD services for files

    @PostMapping("/upload")
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "folderId", required = false) Long folderId, Principal principal){
        try{
            createFileService.uploadFile(file, principal.getName(), folderId);
            return ResponseEntity.ok("File successfully uploaded");
        } catch (IllegalArgumentException | IOException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity<?> deleteFile(@RequestBody DeleteFileRequest request, Principal principal){
        try{
            deleteFileService.deleteFile(request.getFileName(), principal.getName(), request.getFolderId());
            return ResponseEntity.ok("File successfully deleted");
        } catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/replace")
    ResponseEntity<?> replaceFile(@RequestBody ReplaceFileRequest request, Principal principal){
        try {
            replaceFileService.replaceFile(request.getFileName(), request.getCurrentFolderId(), request.getNewFolderId(), principal.getName());
            return ResponseEntity.ok("File successfully replaced");
        } catch (IllegalArgumentException | AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/rename")
    ResponseEntity<?> renameFile(@RequestBody RenameFileRequest request, Principal principal){
        try {
            updateFileService.renameFile(request.getOldFileName(), request.getNewFileName(), request.getFolderId(), principal.getName());
            return ResponseEntity.ok("File successfully renamed");
        } catch (IllegalArgumentException | AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/copy")
    ResponseEntity<?> copyFile(@RequestBody CopyFileRequest request, Principal principal){
        try {
            createFileService.copyFile(request.getFileName(), request.getFolderId(), request.getCopyFolderId(), principal.getName());
            return ResponseEntity.ok("File successfully copied");
        } catch (IllegalArgumentException | IOException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
