package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.SavedJob;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.JobRepository;
import com.JobPortal.JPP.repository.SavedJobRepository;
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
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final SavedJobRepository savedJobRepository;

    @Override
    public JobResponseDTO createJob(JobRequestDTO jobRequestDTO) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExist("User not found"));

        if (user.getRole() != Role.RECRUITER) {
            throw new RuntimeException("Only recruiters can create jobs");
        }

        Job job = new Job();

        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLocation(jobRequestDTO.getLocation());
        job.setSalary(jobRequestDTO.getSalary());
        job.setJobType(jobRequestDTO.getJobType());
        job.setExperienceLevel(jobRequestDTO.getExperienceLevel());
        job.setCreatedAt(LocalDateTime.now());
        job.setRecruiter(user);

        job = jobRepository.save(job);

        return convertToDTO(job);
    }

    @Override
    public JobResponseDTO getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return convertToDTO(job);
    }

    @Override
    public Page<JobResponseDTO> getAllJobs(int page, int size, String sort) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort)
        );

        Page<Job> jobs = jobRepository.findAll(pageable);

        List<JobResponseDTO> response = new ArrayList<>();

        for (Job job : jobs.getContent()) {
            response.add(convertToDTO(job));
        }

        return new PageImpl<>(
                response,
                pageable,
                jobs.getTotalElements()
        );
    }

    @Override
    public JobResponseDTO updateJob(Long id, JobRequestDTO jobRequestDTO) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLocation(jobRequestDTO.getLocation());
        job.setSalary(jobRequestDTO.getSalary());
        job.setJobType(jobRequestDTO.getJobType());
        job.setExperienceLevel(jobRequestDTO.getExperienceLevel());

        job = jobRepository.save(job);

        return convertToDTO(job);
    }

    @Override
    public String removeJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        jobRepository.delete(job);

        return "Job title : " + job.getTitle()
                + " and Id : " + id
                + " has been removed successfully!";
    }

    @Override
    public List<JobResponseDTO> searchJobs(String title) {

        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCase(title);

        List<JobResponseDTO> response = new ArrayList<>();

        for (Job job : jobs) {
            response.add(convertToDTO(job));
        }

        return response;
    }

    @Override
    public String saveJob(Long jobId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new RuntimeException("Only candidates can save jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (savedJobRepository.findByCandidateAndJob(candidate, job).isPresent()) {
            throw new RuntimeException("Job already saved");
        }

        SavedJob savedJob = new SavedJob();

        savedJob.setCandidate(candidate);
        savedJob.setJob(job);

        savedJobRepository.save(savedJob);

        return "Job saved successfully";
    }

    @Override
    public List<JobResponseDTO> getSavedJobs() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        List<SavedJob> savedJobs = savedJobRepository.findByCandidate(candidate);

        List<JobResponseDTO> response = new ArrayList<>();

        for (SavedJob savedJob : savedJobs) {
            response.add(convertToDTO(savedJob.getJob()));
        }

        return response;
    }

    @Override
    public String unsaveJob(Long jobId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        SavedJob savedJob = savedJobRepository
                .findByCandidateAndJob(candidate, job)
                .orElseThrow(() ->
                        new RuntimeException("Saved job not found"));

        savedJobRepository.delete(savedJob);

        return "Job removed from saved jobs";
    }

    @Override
    public List<JobResponseDTO> getMyJobs() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        List<Job> jobs = jobRepository.findByRecruiter(recruiter);

        List<JobResponseDTO> response = new ArrayList<>();

        for (Job job : jobs) {
            response.add(convertToDTO(job));
        }

        return response;
    }

    private JobResponseDTO convertToDTO(Job job) {

        JobResponseDTO dto = new JobResponseDTO();

        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setJobType(job.getJobType());
        dto.setExperienceLevel(job.getExperienceLevel());
        dto.setCreatedAt(job.getCreatedAt());

        // Recruiter details
        dto.setRecruiterName(job.getRecruiter().getName());

        // Uncomment after adding companyName to User
        // dto.setCompanyName(job.getRecruiter().getCompanyName());

        return dto;
    }
}