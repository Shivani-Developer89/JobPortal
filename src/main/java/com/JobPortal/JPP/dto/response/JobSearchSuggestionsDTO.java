package com.JobPortal.JPP.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JobSearchSuggestionsDTO {

    private List<String> jobTitles;
    private List<String> locations;
}