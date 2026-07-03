package com.JobPortal.JPP.controller;


import com.JobPortal.JPP.dto.response.*;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import com.JobPortal.JPP.service.ApplicationService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByJob(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                applicationService.getApplicationsByJob(jobId)
        );
    }
    @PutMapping("/{applicationId}/status")
    public ApplicationResponseDTO updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusDTO request) {

        return applicationService.updateApplicationStatus(
                applicationId,
                request.getStatus());
    }
    @GetMapping("/{applicationId}/resume")
    public ResponseEntity<Resource>
    downloadCandidateResume(
            @PathVariable Long applicationId) {

        Resource resource =
                applicationService
                        .downloadCandidateResume(
                                applicationId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"resume.pdf\"")
                .body(resource);
    }
    @GetMapping("/candidateDashboard")
    public ResponseEntity<CandidateDashboardResponseDTO>
    getCandidateDashboard() {

        return ResponseEntity.ok(
                applicationService
                        .getCandidateDashboard());
    }
    @GetMapping("/recruiterDashboard")
    public RecruiterDashboardResponseDTO getRecruiterDashboard() {
        return applicationService.getRecruiterDashboard();
    }
    @PutMapping("/{applicationId}/withdraw")
    public ApplicationResponseDTO withdrawApplication(
            @PathVariable Long applicationId) {

        return applicationService
                .withdrawApplication(applicationId);
    }
    @GetMapping("/job/{jobId}/applicants")
    public List<RecruiterApplicationResponseDTO>
    viewApplicants(@PathVariable Long jobId){

        return applicationService
                .viewApplicants(jobId);
    }
    @GetMapping("/recruiter/recent")
    public ResponseEntity<List<RecruiterApplicationResponseDTO>>
    getRecentApplications() {

        return ResponseEntity.ok(
                applicationService.getRecentApplications()
        );
    }


}
