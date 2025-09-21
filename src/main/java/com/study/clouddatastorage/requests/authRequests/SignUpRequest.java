package com.study.clouddatastorage.requests.authRequests;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String checkPassword;
}
