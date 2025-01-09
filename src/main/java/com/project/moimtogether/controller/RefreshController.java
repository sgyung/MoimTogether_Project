package com.project.moimtogether.controller;

import com.project.moimtogether.service.RefreshService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "[인증]")
public class RefreshController {
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    @Operation(summary = "리프레시 토큰을 사용하여 엑세스 토큰을 갱신합니다.")
    public ResponseEntity refresh(HttpServletRequest request, HttpServletResponse response) {
        refreshService.refresh(request, response);
        return ResponseEntity.ok().build();
    }
}
