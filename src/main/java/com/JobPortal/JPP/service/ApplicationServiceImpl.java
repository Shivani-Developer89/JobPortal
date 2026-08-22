package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.common.ExperienceDTO;
import com.JobPortal.JPP.dto.response.*;
import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.CandidateProfile;
import com.JobPortal.JPP.entity.Experience;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import com.JobPortal.JPP.entity.enums.JobStatus;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.AccessDeniedException;
import com.JobPortal.JPP.exceptions.AlreadyAppliedException;
import com.JobPortal.JPP.exceptions.ResumeNotFoundException;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.CandidateProfileRepository;
import com.JobPortal.JPP.repository.JobRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CandidateProfileRepository candidateProfileRepository;


    // =========================================================
    // APPLY FOR JOB
    // =========================================================

    @Override
    public ApplicationResponseDTO applyJob(Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new RuntimeException(
                    "Only candidates can apply for jobs");
        }

        if (candidate.getResumePath() == null ||
                candidate.getResumePath().isEmpty()) {

            throw new ResumeNotFoundException(
                    "Please upload resume before applying");
        }

        Job job = jobRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (job.getStatus() == JobStatus.CLOSED) {
            throw new IllegalStateException(
                    "Applications are closed for this job.");
        }

        // Prevent duplicate application
        if (applicationRepository
                .existsByCandidateAndJob(candidate, job)) {

            throw new AlreadyAppliedException(
                    "Already applied");
        }

        Application application = new Application();

        application.setCandidate(candidate);
        application.setJob(job);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);

        application = applicationRepository.save(application);


        // =====================================================
        // SEND EMAIL TO RECRUITER
        // =====================================================

        User recruiter = job.getRecruiter();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String appliedDate =
                application.getAppliedAt().format(formatter);

        String emailBody =
                "Hello Recruiter,\n\n" +
                        "A new candidate has applied for your job posting.\n\n" +
                        "Candidate Name: " + candidate.getName() + "\n" +
                        "Candidate Email: " + candidate.getEmail() + "\n" +
                        "Job Title: " + job.getTitle() + "\n" +
                        "Applied On: " + appliedDate + "\n\n" +
                        "You can review the application from your Job Portal dashboard.\n\n" +
                        "Regards,\nJob Portal Team";

        emailService.sendEmail(
                recruiter.getEmail(),
                "New Job Application - " + job.getTitle(),
                emailBody
        );


        // =====================================================
        // RESPONSE
        // =====================================================

        ApplicationResponseDTO dto =
                new ApplicationResponseDTO();

        dto.setId(application.getId());

        dto.setCandidateId(
                application.getCandidate().getId());

        dto.setJobId(
                application.getJob().getId());

        dto.setJobTitle(
                application.getJob().getTitle());

        dto.setAppliedAt(
                application.getAppliedAt());

        dto.setStatus(
                application.getStatus());

        return dto;
    }


    // =========================================================
    // CANDIDATE - MY APPLICATIONS
    // =========================================================

    @Override
    public List<ApplicationResponseDTO> getMyApplication() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
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

            dto.setJobTitle(
                    application.getJob().getTitle());

            dto.setStatus(
                    application.getStatus());

            dto.setAppliedAt(
                    application.getAppliedAt());

            dtoList.add(dto);
        }

        return dtoList;
    }


    // =========================================================
    // GET APPLICATIONS BY JOB
    // =========================================================

    @Override
    public List<ApplicationResponseDTO> getApplicationsByJob(
            Long jobId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if (user.getRole() != Role.RECRUITER) {
            throw new RuntimeException(
                    "Only recruiters can view applications");
        }

        Job job = jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (!job.getRecruiter()
                .getId()
                .equals(user.getId())) {

            throw new AccessDeniedException(
                    "You can only view applications for your own jobs");
        }

        List<Application> applications =
                applicationRepository.findByJob(job);

        List<ApplicationResponseDTO> response =
                new ArrayList<>();

        for (Application application : applications) {

            ApplicationResponseDTO dto =
                    new ApplicationResponseDTO();

            dto.setId(application.getId());

            dto.setCandidateId(
                    application.getCandidate().getId());

            dto.setJobId(
                    application.getJob().getId());

            dto.setJobTitle(
                    application.getJob().getTitle());

            dto.setStatus(
                    application.getStatus());

            dto.setAppliedAt(
                    application.getAppliedAt());

            response.add(dto);
        }

        return response;
    }


    // =========================================================
    // UPDATE APPLICATION STATUS
    // =========================================================

    @Override
    public ApplicationResponseDTO updateApplicationStatus(
            Long applicationId,
            ApplicationStatus status) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        // Only recruiter can change status
        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException(
                    "Only recruiters can update application status");
        }

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        // IMPORTANT:
        // Recruiter can update only applications
        // belonging to their own job.
        if (!application.getJob()
                .getRecruiter()
                .getId()
                .equals(recruiter.getId())) {

            throw new AccessDeniedException(
                    "You can only update applications for your own jobs");
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Application status cannot be null");
        }

        application.setStatus(status);

        application = applicationRepository.save(application);


        // =====================================================
        // SEND STATUS EMAIL TO CANDIDATE
        // =====================================================

        User candidate = application.getCandidate();

        String jobTitle =
                application.getJob().getTitle();

        String subject;
        String emailBody;

        switch (status) {

            case SHORTLISTED:

                subject =
                        "Application Shortlisted - " + jobTitle;

                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Good news! Your application for " +
                                jobTitle +
                                " has been shortlisted.\n\n" +
                                "The recruiter will contact you regarding " +
                                "the next steps.\n\n" +
                                "Regards,\nJob Portal Team";

                break;


            case HIRED:

                subject =
                        "Congratulations! You've Been Selected";

                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Congratulations!\n\n" +
                                "We are pleased to inform you that you have " +
                                "been selected for the position of " +
                                jobTitle + ".\n\n" +
                                "We wish you success in your new role.\n\n" +
                                "Regards,\nJob Portal Team";

                break;


            case REJECTED:

                subject =
                        "Application Update - " + jobTitle;

                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Thank you for your interest in " +
                                jobTitle + ".\n\n" +
                                "After careful consideration, we have decided " +
                                "to move forward with other candidates.\n\n" +
                                "We wish you success in your future opportunities.\n\n" +
                                "Regards,\nJob Portal Team";

                break;


            default:

                subject =
                        "Application Status Updated";

                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Your application status has been updated to: " +
                                status + ".\n\n" +
                                "Regards,\nJob Portal Team";
        }

        emailService.sendEmail(
                candidate.getEmail(),
                subject,
                emailBody
        );


        // =====================================================
        // RESPONSE
        // =====================================================

        ApplicationResponseDTO dto =
                new ApplicationResponseDTO();

        dto.setId(application.getId());

        dto.setCandidateId(
                application.getCandidate().getId());

        dto.setJobId(
                application.getJob().getId());

        dto.setJobTitle(
                application.getJob().getTitle());

        dto.setAppliedAt(
                application.getAppliedAt());

        dto.setStatus(
                application.getStatus());

        return dto;
    }


    // =========================================================
    // DOWNLOAD / VIEW CANDIDATE RESUME
    // =========================================================

    @Override
    public Resource downloadCandidateResume(Long applicationId) {

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User recruiter =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UserDoesNotExist(
                                        "User not found"));

        // Check recruiter owns this job
        if (!application.getJob()
                .getRecruiter()
                .getId()
                .equals(recruiter.getId())) {

            throw new AccessDeniedException(
                    "You can only view resumes for your own jobs");
        }

        User candidate = application.getCandidate();

        String resumePath = candidate.getResumePath();

        System.out.println("=================================");
        System.out.println("Application ID = " + applicationId);
        System.out.println("Candidate = " + candidate.getName());
        System.out.println("Resume Path = " + resumePath);
        System.out.println("=================================");

        if (resumePath == null ||
                resumePath.trim().isEmpty()) {

            throw new ResumeNotFoundException(
                    "Candidate has not uploaded a resume");
        }

        try {

            Path path = Paths.get(resumePath);

            System.out.println(
                    "Absolute Path = " +
                            path.toAbsolutePath());

            System.out.println(
                    "File Exists = " +
                            Files.exists(path));

            System.out.println(
                    "File Readable = " +
                            Files.isReadable(path));

            if (!Files.exists(path)) {

                throw new ResumeNotFoundException(
                        "Resume file not found at: " +
                                path.toAbsolutePath());
            }

            if (!Files.isReadable(path)) {

                throw new ResumeNotFoundException(
                        "Resume file is not readable");
            }

            Resource resource =
                    new UrlResource(path.toUri());

            if (!resource.exists() ||
                    !resource.isReadable()) {

                throw new ResumeNotFoundException(
                        "Resume resource cannot be read");
            }

            return resource;

        } catch (ResumeNotFoundException e) {

            throw e;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Unable to read resume file",
                    e);
        }
    }


    // =========================================================
    // CANDIDATE DASHBOARD
    // =========================================================

    @Override
    public CandidateDashboardResponseDTO
    getCandidateDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "User not found"));

        CandidateDashboardResponseDTO dto =
                new CandidateDashboardResponseDTO();

        dto.setTotalApplications(
                applicationRepository
                        .countByCandidate(candidate));

        dto.setPendingApplications(
                applicationRepository
                        .countByCandidateAndStatus(
                                candidate,
                                ApplicationStatus.APPLIED));

        dto.setAcceptedApplications(
                applicationRepository
                        .countByCandidateAndStatus(
                                candidate,
                                ApplicationStatus.SHORTLISTED));

        dto.setRejectedApplications(
                applicationRepository
                        .countByCandidateAndStatus(
                                candidate,
                                ApplicationStatus.REJECTED));

        return dto;
    }


    // =========================================================
    // RECRUITER DASHBOARD
    // =========================================================

    @Override
    public RecruiterDashboardResponseDTO
    getRecruiterDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "User not found"));

        if (recruiter.getRole() != Role.RECRUITER) {

            throw new AccessDeniedException(
                    "Only recruiters can access dashboard");
        }

        RecruiterDashboardResponseDTO dto =
                new RecruiterDashboardResponseDTO();

        dto.setTotalJobs(
                jobRepository.countByRecruiter(recruiter));

        dto.setTotalApplications(
                applicationRepository
                        .countByJobRecruiter(recruiter));

        dto.setShortlisted(
                applicationRepository
                        .countByJobRecruiterAndStatus(
                                recruiter,
                                ApplicationStatus.SHORTLISTED));

        dto.setHired(
                applicationRepository
                        .countByJobRecruiterAndStatus(
                                recruiter,
                                ApplicationStatus.HIRED));

        dto.setRejected(
                applicationRepository
                        .countByJobRecruiterAndStatus(
                                recruiter,
                                ApplicationStatus.REJECTED));

        return dto;
    }


    // =========================================================
    // WITHDRAW APPLICATION
    // =========================================================

    @Override
    public ApplicationResponseDTO withdrawApplication(
            Long applicationId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "User not found"));

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        // Candidate can withdraw only their own application
        if (!application.getCandidate()
                .getId()
                .equals(candidate.getId())) {

            throw new AccessDeniedException(
                    "You can only withdraw your own application");
        }

        // Only APPLIED applications can be withdrawn
        if (application.getStatus()
                != ApplicationStatus.APPLIED) {

            throw new RuntimeException(
                    "Only applied applications can be withdrawn");
        }

        application.setStatus(
                ApplicationStatus.WITHDRAWN);

        application =
                applicationRepository.save(application);


        ApplicationResponseDTO dto =
                new ApplicationResponseDTO();

        dto.setId(application.getId());

        dto.setCandidateId(
                application.getCandidate().getId());

        dto.setJobId(
                application.getJob().getId());

        dto.setJobTitle(
                application.getJob().getTitle());

        dto.setAppliedAt(
                application.getAppliedAt());

        dto.setStatus(
                application.getStatus());

        return dto;
    }


    // =========================================================
    // CONVERT APPLICATION -> RECRUITER DTO
    // =========================================================
    private RecruiterApplicationResponseDTO convertToRecruiterDTO(
            Application application) {

        RecruiterApplicationResponseDTO dto =
                new RecruiterApplicationResponseDTO();

        User candidate = application.getCandidate();

        // Application
        dto.setApplicationId(application.getId());

        // Candidate
        dto.setCandidateId(candidate.getId());

        dto.setCandidateName(
                candidate.getName()
        );

        dto.setCandidateEmail(
                candidate.getEmail()
        );

        // Job
        dto.setJobId(
                application.getJob().getId()
        );

        dto.setJobTitle(
                application.getJob().getTitle()
        );

        // Application status
        dto.setStatus(
                application.getStatus()
        );

        dto.setAppliedAt(
                application.getAppliedAt()
        );

        // Resume endpoint
        dto.setResumeUrl(
                "/applications/" +
                        application.getId() +
                        "/resume"
        );

        // Candidate profile
        CandidateProfile profile =
                candidateProfileRepository
                        .findByCandidate(candidate)
                        .orElse(null);

        if (profile != null) {

            // Location
            dto.setCandidateLocation(
                    profile.getLocation()
            );

            // Profile image
            dto.setProfileImagePath(
                    profile.getProfileImagePath()
            );

            // Skills
            dto.setCandidateSkills(
                    profile.getSkills()
            );

            // Experience
            if (profile.getExperience() != null) {

                dto.setCandidateExperience(
                        profile.getExperience()
                                .stream()
                                .map(this::convertExperienceToDTO)
                                .toList()
                );

            } else {

                dto.setCandidateExperience(
                        List.of()
                );
            }
        } else {

            dto.setCandidateLocation(null);

            dto.setProfileImagePath(null);

            dto.setCandidateExperience(
                    List.of()
            );
        }

        return dto;
    }

    // =========================================================
    // EXPERIENCE -> DTO
    // =========================================================

    private ExperienceDTO convertExperienceToDTO(
            Experience experience) {

        ExperienceDTO dto =
                new ExperienceDTO();

        dto.setCompany(
                experience.getCompany());

        dto.setJobTitle(
                experience.getJobTitle());

        dto.setEmploymentType(
                experience.getEmploymentType());

        dto.setLocation(
                experience.getLocation());

        dto.setYearsOfExperience(
                experience.getYearsOfExperience());

        dto.setResponsibilities(
                experience.getResponsibilities());

        dto.setAchievements(
                experience.getAchievements());

        return dto;
    }


    // =========================================================
    // RECRUITER - VIEW APPLICANTS FOR JOB
    // =========================================================

    @Override
    public List<RecruiterApplicationResponseDTO>
    viewApplicants(Long jobId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "User not found"));

        if (recruiter.getRole() != Role.RECRUITER) {

            throw new AccessDeniedException(
                    "Only recruiters can view applicants");
        }

        Job job = jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found"));

        // Important security check
        if (!job.getRecruiter()
                .getId()
                .equals(recruiter.getId())) {

            throw new AccessDeniedException(
                    "You can only view applicants for your own jobs");
        }

        List<Application> applications =
                applicationRepository.findByJob(job);

        return applications
                .stream()
                .map(this::convertToRecruiterDTO)
                .toList();
    }


    // =========================================================
    // RECENT APPLICATIONS
    // =========================================================

    @Override
    public List<RecruiterApplicationResponseDTO>
    getRecentApplications() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "User not found"));

        if (recruiter.getRole() != Role.RECRUITER) {

            throw new AccessDeniedException(
                    "Only recruiters can access recent applications");
        }

        List<Application> applications =
                applicationRepository
                        .findTop10ByJobRecruiterOrderByAppliedAtDesc(
                                recruiter);

        return applications
                .stream()
                .map(this::convertToRecruiterDTO)
                .toList();
    }
}