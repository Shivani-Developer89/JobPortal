package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import com.JobPortal.JPP.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List> getAllJob(){
        return  new ResponseEntity<>(jobService.getAllJob(),HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long id  ,@RequestBody JobRequestDTO jobRequestDTO){
        return new ResponseEntity<>(jobService.updateJob(id , jobRequestDTO),HttpStatusCode.valueOf(200));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeJob(@PathVariable Long id){
        return new ResponseEntity<>(jobService.removeJob(id),HttpStatusCode.valueOf(200));
    }



}
