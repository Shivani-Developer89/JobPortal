package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.RecruiterProfileRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterProfileResponseDTO;
import com.JobPortal.JPP.service.RecruiterProfileService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recruiter-profile")
@RequiredArgsConstructor
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    @PostMapping
    public ResponseEntity<RecruiterProfileResponseDTO>
    createOrUpdateProfile(
            @RequestBody RecruiterProfileRequestDTO request) {

        return ResponseEntity.ok(
                recruiterProfileService
                        .createOrUpdateProfile(request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<RecruiterProfileResponseDTO>
    getMyProfile() {

        return ResponseEntity.ok(
                recruiterProfileService.getMyProfile()
        );
    }

    @PostMapping(
            value = "/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RecruiterProfileResponseDTO>
    uploadProfileImage(
            @RequestParam("image") MultipartFile image) {

        return ResponseEntity.ok(
                recruiterProfileService
                        .uploadProfileImage(image)
        );
    }
}