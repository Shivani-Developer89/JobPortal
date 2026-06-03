package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.User;
import lombok.Data;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "location",
        "salary",
        "createdAt"
})
@Data
public class JobResponseDTO {

    private Long id;
    private String title;

    private String description;
    private String location;

    private Double salary;
    private LocalDateTime createdAt;
}
