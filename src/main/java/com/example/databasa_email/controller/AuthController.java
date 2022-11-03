package com.example.databasa_email.controller;

import com.example.databasa_email.payload.ApiResponse;
import com.example.databasa_email.payload.LoginDto;
import com.example.databasa_email.payload.RegisterDto;
import com.example.databasa_email.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto) {
        ApiResponse register = authService.register(registerDto);
        return ResponseEntity.status(register.isSuccess() ? 201 : 409).body(register);
    }
    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode,@RequestParam String email){
        ApiResponse apiResponse=authService.verifyEmail(emailCode,email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }
    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse=authService.login(loginDto);
        return  ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }
}
