package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.place.PlaceDTO;
import com.project.moimtogether.dto.place.SearchPlaceDTO;
import com.project.moimtogether.service.place.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "[장소 관련 API]")
public class PlaceApiController {

    private final PlaceService placeService;

    @GetMapping("/places")
    @Operation(summary = "모든 장소를 나타낼 수 있습니다.")
    public ResponseEntity<ErrorResponse<List<PlaceDTO>>> findAllPlaces(){
        List<PlaceDTO> placeDTOList = placeService.findAllPlaces();

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,placeDTOList));
    }

    @GetMapping("/places/{placeId}")
    @Operation(summary = "특정 장소를 조회할 수 있습니다.")
    public ResponseEntity<ErrorResponse<SearchPlaceDTO>> findPlace(@PathVariable Long placeId){
        SearchPlaceDTO place = placeService.findPlaceService(placeId);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,place));
    }
}
