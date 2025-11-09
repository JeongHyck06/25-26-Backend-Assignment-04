package com.gdg.jwtexample.controller;

import com.gdg.jwtexample.dto.jwt.TokenRes;
import com.gdg.jwtexample.dto.user.UserLoginReq;
import com.gdg.jwtexample.dto.user.UserSignUpReq;
import com.gdg.jwtexample.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody UserSignUpReq request) {
        Long userId = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@Valid @RequestBody UserLoginReq request) {
        TokenRes tokenRes = authService.login(request);
        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRes> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        TokenRes tokenRes = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        String email = authentication.getName();
        authService.logout(email);
        return ResponseEntity.noContent().build();
    }
}

