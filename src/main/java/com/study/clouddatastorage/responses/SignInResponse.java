package com.study.clouddatastorage.responses;

import lombok.Data;

@Data
public class SignInResponse {
    private String token;
    private String message;
}
