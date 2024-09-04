package com.example.kanban.service;

import com.example.kanban.dto.request.AuthenticationRequest;
import com.example.kanban.dto.response.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    boolean introspect(String token) throws JOSEException, ParseException;
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String generateToken(String username);
}
