package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.response.ApplicationResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplicationService {

    ApplicationResponseDTO applyJob(Long id);
    List<ApplicationResponseDTO> getMyApplication();
}
