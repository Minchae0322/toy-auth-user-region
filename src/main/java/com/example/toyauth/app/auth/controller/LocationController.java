package com.example.toyauth.app.auth.controller;

import com.example.toyauth.app.auth.domain.dto.KakaoSearchLocationResponse;
import com.example.toyauth.app.auth.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "LocationController", description = "위치 서비스")
@RestController(value = "/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("")
    @Operation(summary = "카카오 위치 API",
            tags = "LocationController")
    public ResponseEntity<KakaoSearchLocationResponse> getLocation(@RequestParam @Valid String place) {
        return locationService.getLocationByKeyword(place);
    }

}
