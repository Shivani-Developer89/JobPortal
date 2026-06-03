package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.response.ApplicationResponseDTO;
import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.AccessDeniedException;
import com.JobPortal.JPP.exceptions.AlreadyAppliedException;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
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
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    @Override
    public ApplicationResponseDTO applyJob(Long id) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExist("User not found"));

        if(candidate.getRole() != Role.CANDIDATE){
            throw new RuntimeException("Only candidates can apply for jobs");
        }
        Job job = jobRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Job not found"));
        if (applicationRepository.existsByCandidateAndJob(candidate, job)) {
            throw new AlreadyAppliedException("Already applied");
        }
        Application application = new Application();

        application.setCandidate(candidate);
        application.setJob(job);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);
        application = applicationRepository.save(application);

        ApplicationResponseDTO dto = new ApplicationResponseDTO();


        dto.setId(application.getId());
        dto.setCandidateId(application.getCandidate().getId());
        dto.setJobId(application.getJob().getId());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setStatus(application.getStatus());

        return dto;
    }

    @Override
    public List<ApplicationResponseDTO> getMyApplication() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserDoesNotExist("User not found"));

         List<Application> applications =
                applicationRepository.findByCandidate(user);

        List<ApplicationResponseDTO> dtoList =
                new ArrayList<>();

        for (Application application : applications) {

            ApplicationResponseDTO dto =
                    new ApplicationResponseDTO();

            dto.setId(application.getId());

            dto.setCandidateId(
                    application.getCandidate().getId());

            dto.setJobId(
                    application.getJob().getId());

            dto.setStatus(
                    application.getStatus());

            dto.setAppliedAt(
                    application.getAppliedAt());

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public List<ApplicationResponseDTO> getApplicationsByJob(Long jobId) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        System.out.println("Logged User = " + user.getId());
        System.out.println("Job Owner = " + job.getRecruiter().getId());

        if(user.getRole() != Role.RECRUITER){
            throw new RuntimeException(
                    "Only recruiters can view applications");
        }



        if(!job.getRecruiter().getId().equals(user.getId())){
            throw new AccessDeniedException(
                    "You can only view applications for your own jobs");
        }



        List<Application> applications =
                applicationRepository.findByJob(job);

        List<ApplicationResponseDTO> response = new ArrayList<>();

        for (Application application : applications) {

            ApplicationResponseDTO dto =
                    new ApplicationResponseDTO();

            dto.setId(application.getId());
            dto.setCandidateId(
                    application.getCandidate().getId());
            dto.setJobId(
                    application.getJob().getId());
            dto.setStatus(
                    application.getStatus());
            dto.setAppliedAt(
                    application.getAppliedAt());

            response.add(dto);
        }

        return response;
    }
    @Override
    public ApplicationResponseDTO updateStatus(Long applicationId,
                                               ApplicationStatus status) {

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));

        application.setStatus(status);

        application = applicationRepository.save(application);

        ApplicationResponseDTO dto = new ApplicationResponseDTO();

        dto.setId(application.getId());
        dto.setCandidateId(application.getCandidate().getId());
        dto.setJobId(application.getJob().getId());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setStatus(application.getStatus());

        return dto;
    }

}
