package com.study.clouddatastorage.requests.authRequests;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
