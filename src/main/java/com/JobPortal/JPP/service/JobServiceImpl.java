package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.DashboardResponseDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.JobRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService{
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private  final ApplicationRepository applicationRepository;

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
        job.setRecruiter(user);
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
    public Page<JobResponseDTO> getAllJobs(int page, int size ,   String sort) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sort)
                );

        Page<Job> jobs =
                jobRepository.findAll(pageable);

        List<JobResponseDTO> response =
                new ArrayList<>();

        for (Job job : jobs.getContent()) {

            JobResponseDTO dto =
                    new JobResponseDTO();

            dto.setId(job.getId());
            dto.setTitle(job.getTitle());
            dto.setDescription(job.getDescription());
            dto.setLocation(job.getLocation());
            dto.setSalary(job.getSalary());
            dto.setCreatedAt(job.getCreatedAt());

            response.add(dto);
        }

        return new PageImpl<>(
                response,
                pageable,
                jobs.getTotalElements()
        );
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
    @Override
    public List<JobResponseDTO> searchJobs(String title) {

        List<Job> jobs =
                jobRepository.findByTitleContainingIgnoreCase(title);

        List<JobResponseDTO> response = new ArrayList<>();

        for (Job job : jobs) {

            JobResponseDTO dto = new JobResponseDTO();

            dto.setId(job.getId());
            dto.setTitle(job.getTitle());
            dto.setDescription(job.getDescription());
            dto.setLocation(job.getLocation());
            dto.setSalary(job.getSalary());
            dto.setCreatedAt(job.getCreatedAt());

            response.add(dto);
        }

        return response;
    }
    @Override
    public DashboardResponseDTO getDashboard() {


        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if(recruiter.getRole() != Role.RECRUITER){
            throw new RuntimeException(
                    "Only recruiters can access dashboard");
        }

        DashboardResponseDTO dto =
                new DashboardResponseDTO();

        dto.setTotalJobs(
                jobRepository.countByRecruiter(recruiter));

        dto.setTotalApplications(applicationRepository.countByJobRecruiter(recruiter));

        return dto;
    }
}
