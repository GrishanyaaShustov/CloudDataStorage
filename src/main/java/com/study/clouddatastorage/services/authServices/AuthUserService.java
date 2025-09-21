package com.study.clouddatastorage.services.authServices;

import com.study.clouddatastorage.configuration.jwtConfiguration.JwtProvider;
import com.study.clouddatastorage.models.User;
import com.study.clouddatastorage.repository.UserRepository;
import com.study.clouddatastorage.requests.authRequests.SignInRequest;
import com.study.clouddatastorage.requests.authRequests.SignUpRequest;
import com.study.clouddatastorage.responses.SignInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void signUp(SignUpRequest request){
        if(userRepository.existsUserByEmail(request.getEmail())){
            throw new IllegalArgumentException("Choose different email");
        }
        if(userRepository.existsUserByUsername(request.getUsername())){
            throw new IllegalArgumentException("Choose different username");
        }
        if(!request.getPassword().equals(request.getCheckPassword())){
            throw new IllegalArgumentException("Passwords must be equal");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public SignInResponse signIn(SignInRequest request){
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        SignInResponse response = new SignInResponse();
        response.setToken(jwt);
        response.setMessage("You successfully entered your account");
        return response;
    }
}
