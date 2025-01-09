package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.service.place.RestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestClientController {
    private final RestClientService restClientService;

    @GetMapping("/seoul-data")
    public ResponseEntity<ErrorResponse<String>> getSeoulData(@RequestParam Integer startIndex, @RequestParam Integer endIndex) {
        restClientService.getSeoulData(startIndex, endIndex);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_GET_API_SUCCESS,"성공"));
    }
}
