package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.ScheduleInterviewRequestDTO;
import com.JobPortal.JPP.dto.request.UpdateInterviewRequestDTO;
import com.JobPortal.JPP.dto.response.InterviewResponseDTO;
import com.JobPortal.JPP.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewResponseDTO> scheduleInterview(
            @RequestBody ScheduleInterviewRequestDTO request) {

        return ResponseEntity.ok(
                interviewService.scheduleInterview(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<InterviewResponseDTO>> getMyInterviews() {

        return ResponseEntity.ok(
                interviewService.getMyInterviews());
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponseDTO>>
    getInterviewsByApplication(
            @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                interviewService.getInterviewsByApplication(applicationId));
    }

    @PutMapping("/{interviewId}")
    public ResponseEntity<InterviewResponseDTO> updateInterview(
            @PathVariable Long interviewId,
            @RequestBody UpdateInterviewRequestDTO request) {

        return ResponseEntity.ok(
                interviewService.updateInterview(interviewId, request));
    }

    @PutMapping("/{interviewId}/cancel")
    public ResponseEntity<InterviewResponseDTO> cancelInterview(
            @PathVariable Long interviewId) {

        return ResponseEntity.ok(
                interviewService.cancelInterview(interviewId));
    }

    @PutMapping("/{interviewId}/respond")
    public ResponseEntity<InterviewResponseDTO> respondToInterview(
            @PathVariable Long interviewId,
            @RequestParam String response) {

        return ResponseEntity.ok(
                interviewService.respondToInterview(
                        interviewId,
                        response));
    }

    @PutMapping("/{interviewId}/complete")
    public ResponseEntity<InterviewResponseDTO> completeInterview(
            @PathVariable Long interviewId) {

        return ResponseEntity.ok(
                interviewService.completeInterview(interviewId));
    }
}
