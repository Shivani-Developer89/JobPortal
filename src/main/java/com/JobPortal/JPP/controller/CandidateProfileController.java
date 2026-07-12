package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.CandidateProfileRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;
import com.JobPortal.JPP.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate-profile")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    // Candidate creates or updates profile
    @PostMapping
    public ResponseEntity<CandidateProfileResponseDTO> createOrUpdateProfile(
            @RequestBody CandidateProfileRequestDTO request) {

        System.out.println(">>> Controller reached <<<");

        return ResponseEntity.ok(
                candidateProfileService.createOrUpdateProfile(request)
        );
    }

    // Candidate views own profile
    @GetMapping("/me")
    public ResponseEntity<CandidateProfileResponseDTO> getMyProfile() {

        return ResponseEntity.ok(
                candidateProfileService.getMyProfile()
        );
    }

    // Recruiter views candidate profile
    @GetMapping("/recruiter/candidate/{candidateId}")
    public ResponseEntity<CandidateProfileResponseDTO> getCandidateProfile(
            @PathVariable Long candidateId) {

        return ResponseEntity.ok(
                candidateProfileService.getCandidateProfile(candidateId)
        );
    }
}
