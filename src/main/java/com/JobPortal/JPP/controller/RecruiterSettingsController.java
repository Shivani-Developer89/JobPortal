package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.RecruiterSettingsRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterSettingsResponseDTO;
import com.JobPortal.JPP.service.RecruiterSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruiter/settings")
public class RecruiterSettingsController {

    private final RecruiterSettingsService settingsService;

    public RecruiterSettingsController(
            RecruiterSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<RecruiterSettingsResponseDTO>
    getMySettings() {

        return ResponseEntity.ok(
                settingsService.getMySettings()
        );
    }

    @PutMapping
    public ResponseEntity<RecruiterSettingsResponseDTO>
    updateMySettings(
            @RequestBody RecruiterSettingsRequestDTO request) {

        return ResponseEntity.ok(
                settingsService.updateMySettings(request)
        );
    }
}