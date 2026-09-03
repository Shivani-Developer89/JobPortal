package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.CandidateSettingsRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateSettingsResponseDTO;
import com.JobPortal.JPP.service.CandidateSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate/settings")
public class CandidateSettingsController {

    private final CandidateSettingsService settingsService;

    public CandidateSettingsController(
            CandidateSettingsService settingsService) {

        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<CandidateSettingsResponseDTO>
    getMySettings() {

        return ResponseEntity.ok(
                settingsService.getMySettings()
        );
    }

    @PutMapping
    public ResponseEntity<CandidateSettingsResponseDTO>
    updateMySettings(
            @RequestBody CandidateSettingsRequestDTO request) {

        return ResponseEntity.ok(
                settingsService.updateMySettings(request)
        );
    }
}