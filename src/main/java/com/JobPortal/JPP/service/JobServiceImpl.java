package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.JobRequestDTO;
import com.JobPortal.JPP.dto.response.JobResponseDTO;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.SavedJob;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.JobStatus;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.JobAccessDeniedException;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.JobRepository;
import com.JobPortal.JPP.repository.SavedJobRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final SavedJobRepository savedJobRepository;


    // =========================================================
    // CREATE JOB
    // =========================================================

    @Override
    public JobResponseDTO createJob(JobRequestDTO jobRequestDTO) {

        User recruiter = getCurrentUser();

        requireRecruiter(recruiter);

        Job job = new Job();

        job.setCompanyName(jobRequestDTO.getCompanyName());
        job.setCompanyLogo(jobRequestDTO.getCompanyLogo());

        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLocation(jobRequestDTO.getLocation());

        job.setJobType(jobRequestDTO.getJobType());
        job.setWorkMode(jobRequestDTO.getWorkMode());

        job.setExperienceLevel(
                jobRequestDTO.getExperienceLevel()
        );

        job.setMinExperience(
                jobRequestDTO.getMinExperience()
        );

        job.setMaxExperience(
                jobRequestDTO.getMaxExperience()
        );

        job.setMinSalary(
                jobRequestDTO.getMinSalary()
        );

        job.setMaxSalary(
                jobRequestDTO.getMaxSalary()
        );

        job.setSkills(
                jobRequestDTO.getSkills()
        );

        job.setVacancies(
                jobRequestDTO.getVacancies()
        );

        job.setApplicationDeadline(
                jobRequestDTO.getApplicationDeadline()
        );

        job.setRecruiter(recruiter);

        job = jobRepository.save(job);

        return convertToDTO(job);
    }


    // =========================================================
    // GET SINGLE JOB
    // =========================================================

    @Override
    public JobResponseDTO getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found")
                );

        return convertToDTO(job);
    }


    // =========================================================
    // GET ACTIVE JOBS
    // =========================================================

    @Override
    public Page<JobResponseDTO> getAllJobs(
            int page,
            int size,
            String sort
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort).descending()
        );

        Page<Job> jobs =
                jobRepository.findByStatus(
                        JobStatus.ACTIVE,
                        pageable
                );

        List<JobResponseDTO> response =
                new ArrayList<>();

        for (Job job : jobs.getContent()) {

            response.add(
                    convertToDTO(job)
            );
        }

        return new PageImpl<>(
                response,
                pageable,
                jobs.getTotalElements()
        );
    }


    // =========================================================
    // UPDATE JOB
    // =========================================================

    @Override
    public JobResponseDTO updateJob(
            Long id,
            JobRequestDTO jobRequestDTO
    ) {

        User recruiter = getCurrentUser();

        requireRecruiter(recruiter);

        Job job = getJob(id);

        verifyJobOwnership(
                job,
                recruiter,
                "update"
        );

        job.setCompanyName(
                jobRequestDTO.getCompanyName()
        );

        job.setCompanyLogo(
                jobRequestDTO.getCompanyLogo()
        );

        job.setTitle(
                jobRequestDTO.getTitle()
        );

        job.setDescription(
                jobRequestDTO.getDescription()
        );

        job.setLocation(
                jobRequestDTO.getLocation()
        );

        job.setJobType(
                jobRequestDTO.getJobType()
        );

        job.setWorkMode(
                jobRequestDTO.getWorkMode()
        );

        job.setExperienceLevel(
                jobRequestDTO.getExperienceLevel()
        );

        job.setMinExperience(
                jobRequestDTO.getMinExperience()
        );

        job.setMaxExperience(
                jobRequestDTO.getMaxExperience()
        );

        job.setMinSalary(
                jobRequestDTO.getMinSalary()
        );

        job.setMaxSalary(
                jobRequestDTO.getMaxSalary()
        );

        job.setSkills(
                jobRequestDTO.getSkills()
        );

        job.setVacancies(
                jobRequestDTO.getVacancies()
        );

        job.setApplicationDeadline(
                jobRequestDTO.getApplicationDeadline()
        );

        job = jobRepository.save(job);

        return convertToDTO(job);
    }



    // =========================================================
// DELETE JOB
// =========================================================

    @Override
    public String removeJob(Long id) {

        User recruiter = getCurrentUser();

        // Only recruiters can delete jobs
        requireRecruiter(recruiter);

        Job job = getJob(id);

        // Only the recruiter who created the job can delete it
        verifyJobOwnership(
                job,
                recruiter,
                "delete"
        );

        // Do not physically delete a job if candidates
        // have already applied to it.
        if (applicationRepository.existsByJob_Id(id)) {

            throw new RuntimeException(
                    "Job cannot be deleted because candidates "
                            + "have already applied to this job. "
                            + "Please close the job instead."
            );
        }

        jobRepository.delete(job);

        return "Job title : " + job.getTitle()
                + " and Id : " + id
                + " has been removed successfully!";
    }


    // =========================================================
    // SEARCH JOBS
    // =========================================================

    @Override
    public List<JobResponseDTO> searchJobs(
            String title
    ) {

        List<Job> jobs =
                jobRepository
                        .findByTitleContainingIgnoreCase(title);

        List<JobResponseDTO> response =
                new ArrayList<>();

        for (Job job : jobs) {

            // Don't show closed jobs in search
            if (job.getStatus() != JobStatus.ACTIVE) {
                continue;
            }

            response.add(
                    convertToDTO(job)
            );
        }

        return response;
    }


    // =========================================================
    // SAVE JOB
    // =========================================================

    @Override
    public String saveJob(Long jobId) {

        User candidate = getCurrentUser();

        if (candidate.getRole() != Role.CANDIDATE) {

            throw new JobAccessDeniedException(
                    "Only candidates can save jobs."
            );
        }

        Job job = getJob(jobId);

        if (job.getStatus() != JobStatus.ACTIVE) {

            throw new RuntimeException(
                    "Closed jobs cannot be saved"
            );
        }

        if (
                savedJobRepository
                        .findByCandidateAndJob(
                                candidate,
                                job
                        )
                        .isPresent()
        ) {

            throw new RuntimeException(
                    "Job already saved"
            );
        }

        SavedJob savedJob = new SavedJob();

        savedJob.setCandidate(candidate);
        savedJob.setJob(job);

        savedJobRepository.save(savedJob);

        return "Job saved successfully";
    }


    // =========================================================
    // GET SAVED JOBS
    // =========================================================

    @Override
    public List<JobResponseDTO> getSavedJobs() {

        User candidate = getCurrentUser();

        if (candidate.getRole() != Role.CANDIDATE) {

            throw new JobAccessDeniedException(
                    "Only candidates can view saved jobs."
            );
        }

        List<SavedJob> savedJobs =
                savedJobRepository
                        .findByCandidate(candidate);

        List<JobResponseDTO> response =
                new ArrayList<>();

        for (SavedJob savedJob : savedJobs) {

            response.add(
                    convertToDTO(
                            savedJob.getJob()
                    )
            );
        }

        return response;
    }


    // =========================================================
    // UNSAVE JOB
    // =========================================================

    @Override
    public String unsaveJob(Long jobId) {

        User candidate = getCurrentUser();

        if (candidate.getRole() != Role.CANDIDATE) {

            throw new JobAccessDeniedException(
                    "Only candidates can unsave jobs."
            );
        }

        Job job = getJob(jobId);

        SavedJob savedJob =
                savedJobRepository
                        .findByCandidateAndJob(
                                candidate,
                                job
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Saved job not found"
                                )
                        );

        savedJobRepository.delete(savedJob);

        return "Job removed from saved jobs";
    }


    // =========================================================
    // GET RECRUITER JOBS
    // =========================================================

    @Override
    public List<JobResponseDTO> getMyJobs() {

        User recruiter = getCurrentUser();

        requireRecruiter(recruiter);

        List<Job> jobs =
                jobRepository.findByRecruiter(
                        recruiter
                );

        List<JobResponseDTO> response =
                new ArrayList<>();

        for (Job job : jobs) {

            response.add(
                    convertToDTO(job)
            );
        }

        return response;
    }


    // =========================================================
    // CLOSE JOB
    // =========================================================

    @Override
    public JobResponseDTO closeJob(
            Long jobId
    ) {

        User recruiter = getCurrentUser();

        requireRecruiter(recruiter);

        Job job = getJob(jobId);

        verifyJobOwnership(
                job,
                recruiter,
                "close"
        );

        job.setStatus(
                JobStatus.CLOSED
        );

        Job savedJob =
                jobRepository.save(job);

        return convertToDTO(savedJob);
    }


    // =========================================================
    // REOPEN JOB
    // =========================================================

    @Override
    public JobResponseDTO reopenJob(
            Long jobId
    ) {

        User recruiter = getCurrentUser();

        requireRecruiter(recruiter);

        Job job = getJob(jobId);

        verifyJobOwnership(
                job,
                recruiter,
                "reopen"
        );

        job.setStatus(
                JobStatus.ACTIVE
        );

        Job savedJob =
                jobRepository.save(job);

        return convertToDTO(savedJob);
    }


    // =========================================================
    // CONVERT ENTITY → DTO
    // =========================================================

    private JobResponseDTO convertToDTO(
            Job job
    ) {

        JobResponseDTO dto =
                new JobResponseDTO();

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication != null
                        && authentication.isAuthenticated()
                        && !"anonymousUser".equals(
                        authentication.getName()
                )
        ) {

            User currentUser =
                    userRepository
                            .findByEmail(
                                    authentication.getName()
                            )
                            .orElse(null);

            if (currentUser != null) {

                boolean applied =
                        applicationRepository
                                .existsByCandidateAndJob(
                                        currentUser,
                                        job
                                );

                dto.setApplied(applied);
            }
        }

        dto.setId(job.getId());

        dto.setCompanyName(
                job.getCompanyName()
        );

        dto.setCompanyLogo(
                job.getCompanyLogo()
        );

        dto.setTitle(
                job.getTitle()
        );

        dto.setDescription(
                job.getDescription()
        );

        dto.setLocation(
                job.getLocation()
        );

        dto.setJobType(
                job.getJobType()
        );

        dto.setWorkMode(
                job.getWorkMode()
        );

        dto.setExperienceLevel(
                job.getExperienceLevel()
        );

        dto.setMinExperience(
                job.getMinExperience()
        );

        dto.setMaxExperience(
                job.getMaxExperience()
        );

        dto.setMinSalary(
                job.getMinSalary()
        );

        dto.setMaxSalary(
                job.getMaxSalary()
        );

        dto.setSkills(
                job.getSkills()
        );

        dto.setVacancies(
                job.getVacancies()
        );

        dto.setApplicationDeadline(
                job.getApplicationDeadline()
        );

        dto.setCreatedAt(
                job.getCreatedAt()
        );

        dto.setUpdatedAt(
                job.getUpdatedAt()
        );

        if (job.getRecruiter() != null) {

            dto.setRecruiterId(
                    job.getRecruiter().getId()
            );

            dto.setRecruiterName(
                    job.getRecruiter().getName()
            );
        }

        dto.setStatus(
                job.getStatus()
        );

        return dto;
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "User not found"
                        )
                );
    }


    private Job getJob(Long id) {

        return jobRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found"
                        )
                );
    }


    private void requireRecruiter(User user) {

        if (user.getRole() != Role.RECRUITER) {

            throw new JobAccessDeniedException(
                    "Only recruiters can perform this action."
            );
        }
    }


    private void verifyJobOwnership(
            Job job,
            User recruiter,
            String action
    ) {

        if (
                job.getRecruiter() == null
                        || !job.getRecruiter()
                        .getId()
                        .equals(recruiter.getId())
        ) {

            throw new JobAccessDeniedException(
                    "You are not allowed to " + action + " this job. "
                            + "Only the recruiter who created it can "
                            + action + " it."
            );
        }
    }
}