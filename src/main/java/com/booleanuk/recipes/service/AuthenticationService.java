package com.booleanuk.recipes.service;


import com.booleanuk.recipes.dao.request.SignUpRequest;
import com.booleanuk.recipes.dao.request.SigninRequest;
import com.booleanuk.recipes.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
