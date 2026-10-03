package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.NotificationRequestDTO;
import com.JobPortal.JPP.dto.request.ScheduleInterviewRequestDTO;
import com.JobPortal.JPP.dto.request.UpdateInterviewRequestDTO;
import com.JobPortal.JPP.dto.response.InterviewResponseDTO;
import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.Interview;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import com.JobPortal.JPP.entity.enums.InterviewResponse;
import com.JobPortal.JPP.entity.enums.InterviewStatus;
import com.JobPortal.JPP.entity.enums.InterviewType;
import com.JobPortal.JPP.entity.enums.NotificationType;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.InterviewRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public InterviewResponseDTO scheduleInterview(
            ScheduleInterviewRequestDTO request) {

        User recruiter = getCurrentUser();
        requireRole(recruiter, Role.RECRUITER);
        validateScheduleRequest(request);

        Application application = applicationRepository
                .findById(request.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Job job = application.getJob();

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You are not allowed to schedule this interview");
        }

        if (application.getStatus() == ApplicationStatus.REJECTED ||
                application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new IllegalStateException(
                    "Interview cannot be scheduled for this application");
        }

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setScheduledAt(request.getScheduledAt());
        interview.setDurationMinutes(request.getDurationMinutes());
        interview.setType(request.getType());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setLocation(request.getLocation());
        interview.setNotes(request.getNotes());
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCandidateResponse(InterviewResponse.PENDING);

        interview = interviewRepository.save(interview);

        notifyCandidate(
                application,
                "Interview Scheduled",
                "Your interview for " + job.getTitle() +
                        " is scheduled for " +
                        interview.getScheduledAt().format(DISPLAY_FORMAT) + ".",
                NotificationType.INTERVIEW_SCHEDULED
        );

        return toDTO(interview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponseDTO> getMyInterviews() {

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.CANDIDATE) {
            return interviewRepository
                    .findByCandidateIdOrderByScheduledAtAsc(currentUser.getId())
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }

        if (currentUser.getRole() == Role.RECRUITER) {
            return interviewRepository
                    .findByApplicationJobRecruiterOrderByScheduledAtAsc(currentUser)
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }

        throw new RuntimeException("Unsupported user role");
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponseDTO> getInterviewsByApplication(Long applicationId) {

        User currentUser = getCurrentUser();

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        ensureApplicationAccess(application, currentUser);

        return interviewRepository.findByApplication(application)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public InterviewResponseDTO updateInterview(
            Long interviewId,
            UpdateInterviewRequestDTO request) {

        User recruiter = getCurrentUser();
        requireRole(recruiter, Role.RECRUITER);
        validateUpdateRequest(request);

        Interview interview = getInterview(interviewId);
        Application application = interview.getApplication();

        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You are not allowed to update this interview");
        }

        if (interview.getStatus() == InterviewStatus.CANCELLED ||
                interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Cancelled or completed interviews cannot be updated");
        }

        interview.setScheduledAt(request.getScheduledAt());
        interview.setDurationMinutes(request.getDurationMinutes());
        interview.setType(request.getType());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setLocation(request.getLocation());
        interview.setNotes(request.getNotes());
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCandidateResponse(InterviewResponse.PENDING);

        interview = interviewRepository.save(interview);

        notifyCandidate(
                application,
                "Interview Updated",
                "Your interview for " + application.getJob().getTitle() +
                        " has been updated to " +
                        interview.getScheduledAt().format(DISPLAY_FORMAT) + ".",
                NotificationType.INTERVIEW_UPDATED
        );

        return toDTO(interview);
    }

    @Override
    public InterviewResponseDTO cancelInterview(Long interviewId) {

        User recruiter = getCurrentUser();
        requireRole(recruiter, Role.RECRUITER);

        Interview interview = getInterview(interviewId);
        Application application = interview.getApplication();

        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("You are not allowed to cancel this interview");
        }

        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            return toDTO(interview);
        }

        interview.setStatus(InterviewStatus.CANCELLED);
        interview = interviewRepository.save(interview);

        notifyCandidate(
                application,
                "Interview Cancelled",
                "Your interview for " + application.getJob().getTitle() +
                        " has been cancelled.",
                NotificationType.INTERVIEW_CANCELLED
        );

        return toDTO(interview);
    }

    @Override
    public InterviewResponseDTO respondToInterview(
            Long interviewId,
            String response) {

        User candidate = getCurrentUser();
        requireRole(candidate, Role.CANDIDATE);

        Interview interview = getInterview(interviewId);
        Application application = interview.getApplication();

        if (!application.getCandidate().getId().equals(candidate.getId())) {
            throw new RuntimeException("You are not allowed to respond to this interview");
        }

        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            throw new IllegalStateException(
                    "Only scheduled interviews can be accepted or declined");
        }

        InterviewResponse interviewResponse;
        try {
            interviewResponse = InterviewResponse.valueOf(response.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Response must be ACCEPTED or DECLINED");
        }

        if (interviewResponse == InterviewResponse.PENDING) {
            throw new IllegalArgumentException(
                    "Response must be ACCEPTED or DECLINED");
        }

        interview.setCandidateResponse(interviewResponse);
        interview = interviewRepository.save(interview);

        String candidateResponseText =
                interviewResponse == InterviewResponse.ACCEPTED
                        ? "accepted"
                        : "declined";

        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setUserId(application.getJob().getRecruiter().getId());
        notification.setTitle("Interview Response Received");
        notification.setMessage(
                application.getCandidate().getName() +
                        " has " + candidateResponseText +
                        " the interview for " +
                        application.getJob().getTitle() + "."
        );
        notification.setType(NotificationType.INTERVIEW_UPDATED);
        notificationService.createNotification(notification);

        return toDTO(interview);
    }

    @Override
    public InterviewResponseDTO completeInterview(Long interviewId) {

        User recruiter = getCurrentUser();
        requireRole(recruiter, Role.RECRUITER);

        Interview interview = getInterview(interviewId);

        if (!interview.getApplication().getJob().getRecruiter().getId()
                .equals(recruiter.getId())) {
            throw new RuntimeException("You are not allowed to complete this interview");
        }

        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled interviews cannot be completed");
        }

        interview.setStatus(InterviewStatus.COMPLETED);
        interview = interviewRepository.save(interview);

        return toDTO(interview);
    }

    private Interview getInterview(Long interviewId) {
        return interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void requireRole(User user, Role role) {
        if (user.getRole() != role) {
            throw new RuntimeException("Only " + role.name().toLowerCase() + "s can perform this action");
        }
    }

    private void ensureApplicationAccess(Application application, User user) {

        boolean candidateAccess =
                user.getRole() == Role.CANDIDATE &&
                        application.getCandidate().getId().equals(user.getId());

        boolean recruiterAccess =
                user.getRole() == Role.RECRUITER &&
                        application.getJob().getRecruiter().getId().equals(user.getId());

        if (!candidateAccess && !recruiterAccess) {
            throw new RuntimeException("You are not allowed to access this application");
        }
    }

    private void validateScheduleRequest(ScheduleInterviewRequestDTO request) {

        if (request.getApplicationId() == null) {
            throw new IllegalArgumentException("Application ID is required");
        }

        if (request.getScheduledAt() == null) {
            throw new IllegalArgumentException("Interview date and time are required");
        }

        if (!request.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Interview date and time must be in the future");
        }

        if (request.getDurationMinutes() == null ||
                request.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0 minutes");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("Interview type is required");
        }

        validateTypeDetails(
                request.getType(),
                request.getMeetingLink(),
                request.getLocation());
    }

    private void validateUpdateRequest(UpdateInterviewRequestDTO request) {

        if (request.getScheduledAt() == null) {
            throw new IllegalArgumentException("Interview date and time are required");
        }

        if (!request.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Interview date and time must be in the future");
        }

        if (request.getDurationMinutes() == null ||
                request.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0 minutes");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("Interview type is required");
        }

        validateTypeDetails(
                request.getType(),
                request.getMeetingLink(),
                request.getLocation());
    }

    private void validateTypeDetails(
            InterviewType type,
            String meetingLink,
            String location) {

        if (type == InterviewType.ONLINE &&
                (meetingLink == null || meetingLink.isBlank())) {
            throw new IllegalArgumentException(
                    "Meeting link is required for online interviews");
        }

        if (type == InterviewType.IN_PERSON &&
                (location == null || location.isBlank())) {
            throw new IllegalArgumentException(
                    "Location is required for in-person interviews");
        }
    }

    private void notifyCandidate(
            Application application,
            String title,
            String message,
            NotificationType type) {

        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setUserId(application.getCandidate().getId());
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notificationService.createNotification(notification);
    }

    private InterviewResponseDTO toDTO(Interview interview) {

        Application application = interview.getApplication();
        Job job = application.getJob();

        InterviewResponseDTO dto = new InterviewResponseDTO();

        dto.setId(interview.getId());
        dto.setApplicationId(application.getId());
        dto.setJobId(job.getId());
        dto.setJobTitle(job.getTitle());

        dto.setCandidateId(application.getCandidate().getId());
        dto.setCandidateName(application.getCandidate().getName());

        dto.setRecruiterId(job.getRecruiter().getId());
        dto.setRecruiterName(job.getRecruiter().getName());

        dto.setScheduledAt(interview.getScheduledAt());
        dto.setDurationMinutes(interview.getDurationMinutes());
        dto.setType(interview.getType());
        dto.setStatus(interview.getStatus());
        dto.setCandidateResponse(interview.getCandidateResponse());
        dto.setMeetingLink(interview.getMeetingLink());
        dto.setLocation(interview.getLocation());
        dto.setNotes(interview.getNotes());
        dto.setCreatedAt(interview.getCreatedAt());
        dto.setUpdatedAt(interview.getUpdatedAt());

        return dto;
    }
}
