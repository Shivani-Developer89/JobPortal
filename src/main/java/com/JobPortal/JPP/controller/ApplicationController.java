package com.JobPortal.JPP.controller;


import com.JobPortal.JPP.dto.response.ApplicationResponseDTO;
import com.JobPortal.JPP.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<ApplicationResponseDTO> applyJob(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                applicationService.applyJob(jobId)
        );
    }
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponseDTO>> getMyApplications(){
        return ResponseEntity.ok(
                applicationService.getMyApplication());
    }
}
