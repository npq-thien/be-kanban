package com.example.kanban.controller;

import com.example.kanban.dto.request.AuthenticationRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/introspect")
    ResponseEntity<ApiResponse> introspect(@RequestBody String token) throws ParseException, JOSEException {
        boolean isValid = authenticationService.introspect(token);
        String validateMessage = isValid ? "Token is valid" : "Token is invalid";

        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put("valid", isValid);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message(validateMessage)
                .data(resultMap)
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/token")
    ResponseEntity<ApiResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Authenticated")
                .data(result)
                .timestamp(new Date())
                .build();

        return ResponseEntity.accepted().body(apiResponse);
    }
}
