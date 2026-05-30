package com.JobPortal.JPP.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobResponseDTO {

    private Long id;
    private String title;

    private String description;
    private String location;
    private Double Salary;
    private LocalDate createdAt;
}
