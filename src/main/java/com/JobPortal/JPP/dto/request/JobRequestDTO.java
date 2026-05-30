package com.JobPortal.JPP.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data

public class JobRequestDTO {

    private String title;
    private String description;
    private String location;
    private Double Salary;
    private LocalDate createdAt;
}
