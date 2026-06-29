package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import com.JobPortal.JPP.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RestControllerAdvice
@RequestMapping("/job")
public class JobController {
    @Autowired
    JobService jobService;

    @PostMapping()
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody JobRequestDTO jobRequestDTO){
        return  new ResponseEntity<>(jobService.createJob(jobRequestDTO), HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJob(@PathVariable Long id){
        return  new ResponseEntity<>(jobService.getJobById(id),HttpStatusCode.valueOf(200));
    }
    @GetMapping("/all")
    public ResponseEntity<Page<JobResponseDTO>> getAllJobs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,
            @RequestParam(defaultValue = "id")
                    String sort
    ) {

        return ResponseEntity.ok(
                jobService.getAllJobs(
                        page,
                        size,
                        sort));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long id  ,@RequestBody JobRequestDTO jobRequestDTO){
        return new ResponseEntity<>(jobService.updateJob(id , jobRequestDTO),HttpStatusCode.valueOf(200));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeJob(@PathVariable Long id){
        return new ResponseEntity<>(jobService.removeJob(id),HttpStatusCode.valueOf(200));
    }
    @GetMapping("/search")
    public ResponseEntity<List<JobResponseDTO>> searchJobs(
            @RequestParam String title) {

        return ResponseEntity.ok(
                jobService.searchJobs(title));
    }
    @GetMapping("/my")
    public ResponseEntity<List<JobResponseDTO>> getMyJobs() {

        return ResponseEntity.ok(
                jobService.getMyJobs()
        );
    }
    @PostMapping("/{jobId}/save")
    public ResponseEntity<String> saveJob(@PathVariable Long jobId){
        return ResponseEntity.ok(jobService.saveJob(jobId));
    }
    @GetMapping("/saved")
    public ResponseEntity<List<JobResponseDTO>> getSavedJobs() {
        return ResponseEntity.ok(
                jobService.getSavedJobs()
        );
    }
    @DeleteMapping("/{jobId}/unsave")
    public ResponseEntity<String> unsaveJob(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                jobService.unsaveJob(jobId));
    }



}
