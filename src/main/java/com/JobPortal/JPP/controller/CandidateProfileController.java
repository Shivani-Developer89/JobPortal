package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.CandidateProfileRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;
import com.JobPortal.JPP.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(
            value = "/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CandidateProfileResponseDTO> uploadProfileImage(
            @RequestParam("image") MultipartFile image
    ) {

        CandidateProfileResponseDTO response =
                candidateProfileService.uploadProfileImage(image);

        return ResponseEntity.ok(response);
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
    @GetMapping("/candidate/{candidateId}/profile-image")
    public ResponseEntity<Resource> getProfileImage(
            @PathVariable Long candidateId) {

        Resource resource =
                candidateProfileService.getProfileImage(candidateId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline"
                )
                .body(resource);
    }
}
