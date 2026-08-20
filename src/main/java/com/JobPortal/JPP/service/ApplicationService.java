package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.response.*;
import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ApplicationService {

    ApplicationResponseDTO applyJob(Long id);

    List<ApplicationResponseDTO> getMyApplication();

    List<ApplicationResponseDTO> getApplicationsByJob(Long jobId);

    ApplicationResponseDTO updateApplicationStatus(
            Long applicationId,
            ApplicationStatus status);

    Resource downloadCandidateResume(Long applicationId);

    CandidateDashboardResponseDTO getCandidateDashboard();

    RecruiterDashboardResponseDTO getRecruiterDashboard();

    ApplicationResponseDTO withdrawApplication(Long applicationId);

    List<RecruiterApplicationResponseDTO> viewApplicants(Long jobId);

    List<RecruiterApplicationResponseDTO> getRecentApplications();
}