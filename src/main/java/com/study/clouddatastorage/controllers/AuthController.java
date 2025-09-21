package com.study.clouddatastorage.controllers;

import com.study.clouddatastorage.requests.authRequests.SignInRequest;
import com.study.clouddatastorage.requests.authRequests.SignUpRequest;
import com.study.clouddatastorage.responses.SignInResponse;
import com.study.clouddatastorage.services.authServices.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignUpRequest request){
        try {
            authUserService.signUp(request);
            return ResponseEntity.ok("User successfully created!");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            SignInResponse response = authUserService.signIn(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
