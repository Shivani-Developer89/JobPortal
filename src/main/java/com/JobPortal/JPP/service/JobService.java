package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;

import java.util.List;

public interface JobService {
    JobResponseDTO createJob(JobRequestDTO jobRequestDTO);
    JobResponseDTO getJobById(Long id);
    List<JobResponseDTO> getAllJob();
    JobResponseDTO updateJob(Long id , JobRequestDTO jobRequestDTO);
    String removeJob(Long id);


}
