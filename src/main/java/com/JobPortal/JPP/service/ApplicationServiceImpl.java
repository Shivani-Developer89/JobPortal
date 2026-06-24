package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.response.ApplicationResponseDTO;
import com.JobPortal.JPP.dto.response.CandidateDashboardResponseDTO;
import com.JobPortal.JPP.dto.response.RecruiterApplicationResponseDTO;
import com.JobPortal.JPP.dto.response.RecruiterDashboardResponseDTO;
import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.AccessDeniedException;
import com.JobPortal.JPP.exceptions.AlreadyAppliedException;
import com.JobPortal.JPP.exceptions.ResumeNotFoundException;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.JobRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        if(candidate.getResumePath() == null ||
                candidate.getResumePath().isEmpty()) {
            throw new ResumeNotFoundException(
                    "Please upload resume before applying"
            );
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


        System.out.println("Application saved");

        User recruiter = job.getRecruiter();

        System.out.println("Recruiter email: " + recruiter.getEmail());
        // format date only for email
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
                        "Regards,\n" +
                        "Job Portal Team";

        emailService.sendEmail(
                recruiter.getEmail(),
                "New Job Application - " + job.getTitle(),
                emailBody

        );

        System.out.println("Email method executed");

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
    public ApplicationResponseDTO updateApplicationStatus(Long applicationId,
                                               ApplicationStatus status) {

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));

        application.setStatus(status);

        application = applicationRepository.save(application);


        User candidate = application.getCandidate();
        String jobTitle = application.getJob().getTitle();

        String subject;
        String emailBody;

        switch (status) {

            case SHORTLISTED:
                subject = "Application Shortlisted - " + jobTitle;
                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Good news! Your application for " + jobTitle +
                                " has been shortlisted.\n\n" +
                                "The recruiter will contact you regarding the next steps.\n\n" +
                                "Regards,\nJob Portal Team";
                break;

            case HIRED:
                subject = "Congratulations! You've Been Selected";
                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Congratulations!\n\n" +
                                "We are pleased to inform you that you have been selected for the position of "
                                + jobTitle + ".\n\n" +
                                "We wish you success in your new role.\n\n" +
                                "Regards,\nJob Portal Team";
                break;

            case REJECTED:
                subject = "Application Update - " + jobTitle;
                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Thank you for your interest in " + jobTitle + ".\n\n" +
                                "After careful consideration, we have decided to move forward with other candidates.\n\n" +
                                "We wish you success in your future opportunities.\n\n" +
                                "Regards,\nJob Portal Team";
                break;

            default:
                subject = "Application Status Updated";
                emailBody =
                        "Hello " + candidate.getName() + ",\n\n" +
                                "Your application status has been updated to: "
                                + status + ".\n\n" +
                                "Regards,\nJob Portal Team";
        }
        emailService.sendEmail(
                candidate.getEmail(),
                subject,
                emailBody
        );
        ApplicationResponseDTO dto = new ApplicationResponseDTO();

        dto.setId(application.getId());
        dto.setCandidateId(application.getCandidate().getId());
        dto.setJobId(application.getJob().getId());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setStatus(application.getStatus());

        return dto;
    }

    @Override
    public Resource downloadCandidateResume(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application not found"));

        String email = SecurityContextHolder
                        .getContext()
                .getAuthentication()
                .getName();
        User recruiter = userRepository.findByEmail(email).orElseThrow(() ->new UserDoesNotExist());

        if(!application.getJob()
                .getRecruiter()
                .getId()
                .equals(recruiter.getId()) ){
            throw new AccessDeniedException("You can download resumes f your own job");

        }
        User candidate = application.getCandidate();
        String resumePath = candidate.getResumePath();
        if(resumePath == null || resumePath.isEmpty()){
            throw new RuntimeException("Candidate has not uploaded a resume");
        }
        try {
            Path path = Paths.get(resumePath);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("Resume file not found");
            }

            return resource;

        } catch (Exception e) {
            throw new RuntimeException("Resume not found");
        }

    }
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
                                ApplicationStatus.HIRED));

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
    @Override
    public RecruiterDashboardResponseDTO getRecruiterDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if(recruiter.getRole() != Role.RECRUITER){
            throw new RuntimeException(
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

    @Override
    public ApplicationResponseDTO withdrawApplication(Long applicationId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        if(!application.getCandidate().getId()
                .equals(candidate.getId())) {
            throw new RuntimeException(
                    "You can only withdraw your own application");
        }

        if(application.getStatus() == ApplicationStatus.HIRED) {
            throw new RuntimeException(
                    "Cannot withdraw a hired application");
        }

        if(application.getStatus() == ApplicationStatus.REJECTED) {
            throw new RuntimeException(
                    "Cannot withdraw a rejected application");
        }

        if(application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new RuntimeException(
                    "Application already withdrawn");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);

        application = applicationRepository.save(application);

        ApplicationResponseDTO dto =
                new ApplicationResponseDTO();

        dto.setId(application.getId());
        dto.setCandidateId(application.getCandidate().getId());
        dto.setJobId(application.getJob().getId());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setStatus(application.getStatus());

        return dto;
    }
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
                        new UserDoesNotExist("User not found"));

        if(recruiter.getRole() != Role.RECRUITER){
            throw new RuntimeException(
                    "Only recruiters can view applicants");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if(!job.getRecruiter().getId()
                .equals(recruiter.getId())) {

            throw new RuntimeException(
                    "You can only view applicants for your own jobs");
        }

        List<Application> applications =
                applicationRepository.findByJob(job);

        List<RecruiterApplicationResponseDTO> response =
                new ArrayList<>();

        for(Application application : applications){

            RecruiterApplicationResponseDTO dto =
                    new RecruiterApplicationResponseDTO();

            dto.setApplicationId(application.getId());
            dto.setCandidateName(
                    application.getCandidate().getName());
            dto.setCandidateEmail(
                    application.getCandidate().getEmail());
            dto.setStatus(application.getStatus());
            dto.setAppliedAt(application.getAppliedAt());
            dto.setResumeUrl(
                    "/applications/" + application.getId() + "/resume"
            );

            response.add(dto);
        }

        return response;
    }




}
