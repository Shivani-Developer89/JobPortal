package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

public class JobRequestDTO {

    private String title;
    private String description;
    private String location;

    private Double salary;
    private LocalDateTime createdAt;
}
