package com.study.clouddatastorage.controllers;

import com.study.clouddatastorage.services.fileService.CreateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private CreateFileService createFileService;

    @PostMapping("/upload")
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "folderId", required = false) Long folderId, Principal principal){
        try{
            createFileService.uploadFile(file, principal.getName(), folderId);
            return ResponseEntity.ok("File successfully uploaded");
        } catch (IllegalArgumentException | IOException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
