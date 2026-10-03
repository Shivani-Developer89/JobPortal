package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.ScheduleInterviewRequestDTO;
import com.JobPortal.JPP.dto.request.UpdateInterviewRequestDTO;
import com.JobPortal.JPP.dto.response.InterviewResponseDTO;

import java.util.List;

public interface InterviewService {

    InterviewResponseDTO scheduleInterview(ScheduleInterviewRequestDTO request);

    List<InterviewResponseDTO> getMyInterviews();

    List<InterviewResponseDTO> getInterviewsByApplication(Long applicationId);

    InterviewResponseDTO updateInterview(Long interviewId, UpdateInterviewRequestDTO request);

    InterviewResponseDTO cancelInterview(Long interviewId);

    InterviewResponseDTO respondToInterview(Long interviewId, String response);

    InterviewResponseDTO completeInterview(Long interviewId);
}
