package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.JobRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService{
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public JobResponseDTO createJob(JobRequestDTO jobRequestDTO) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExist("User not found"));

        if(user.getRole() != Role.RECRUITER){
            throw new RuntimeException("Only recruiters can create jobs");
        }
        Job job = new Job();
        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLocation(jobRequestDTO.getLocation());
        job.setSalary(jobRequestDTO.getSalary());
        job.setCreatedAt(LocalDateTime.now());

        job = jobRepository.save(job);

        JobResponseDTO jobResponseDTO = new JobResponseDTO();

        jobResponseDTO.setId(job.getId());
        jobResponseDTO.setTitle(job.getTitle());
        jobResponseDTO.setDescription(job.getDescription());
        jobResponseDTO.setLocation(job.getLocation());
        jobResponseDTO.setSalary(job.getSalary());
        jobResponseDTO.setCreatedAt(job.getCreatedAt());

        return jobResponseDTO;
    }

    @Override
    public JobResponseDTO getJobById(Long id) {
        Job job =  jobRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Job not found"));
        JobResponseDTO dto = new JobResponseDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setCreatedAt(job.getCreatedAt());

        return dto;
    }

    @Override
    public List<JobResponseDTO> getAllJob() {
        List<Job> JobList = jobRepository.findAll();
        List<JobResponseDTO> jobResponseDTOList = new ArrayList<>();
        for (Job job : JobList){
            JobResponseDTO jobResponseDTO = new JobResponseDTO();

            jobResponseDTO.setId(job.getId());
            jobResponseDTO.setTitle(job.getTitle());
            jobResponseDTO.setDescription(job.getDescription());
            jobResponseDTO.setLocation(job.getLocation());
            jobResponseDTO.setSalary(job.getSalary());
            jobResponseDTO.setCreatedAt(job.getCreatedAt());

            jobResponseDTOList.add(jobResponseDTO);

        }
        return jobResponseDTOList;
    }

    @Override
    public JobResponseDTO updateJob(Long id ,JobRequestDTO jobRequestDTO ) {
        Job job = new Job();
        job.setId(id);
        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLocation(jobRequestDTO.getLocation());
        job.setSalary(jobRequestDTO.getSalary());
        job.setCreatedAt(jobRequestDTO.getCreatedAt());

        job = jobRepository.save(job);

        JobResponseDTO jobResponseDTO = new JobResponseDTO();

        jobResponseDTO.setId(job.getId());
        jobResponseDTO.setTitle(job.getTitle());
        jobResponseDTO.setDescription(job.getDescription());
        jobResponseDTO.setLocation(job.getLocation());
        jobResponseDTO.setSalary(job.getSalary());
        jobResponseDTO.setCreatedAt(job.getCreatedAt());

        return jobResponseDTO;


    }

    @Override
    public String removeJob(Long id) {
        String name = jobRepository.findById(id).orElse(null).getTitle();
        jobRepository.deleteById(id);
        return "Job title : " +  name + " and Id : "  +id  +   " has been removed successfully! ";
    }
}
