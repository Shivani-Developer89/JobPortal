package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.DashboardResponseDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface JobService {
    JobResponseDTO createJob(JobRequestDTO jobRequestDTO);
    JobResponseDTO getJobById(Long id);
    Page<JobResponseDTO> getAllJobs(int page, int size,String sort);
    JobResponseDTO updateJob(Long id , JobRequestDTO jobRequestDTO);
    String removeJob(Long id);
    List<JobResponseDTO> searchJobs(String title);
    DashboardResponseDTO getDashboard();
    String saveJob(Long jobId);
    List<JobResponseDTO> getSavedJobs();
    String unsaveJob(Long jobId);




}
