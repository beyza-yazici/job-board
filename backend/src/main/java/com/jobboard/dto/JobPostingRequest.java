package com.jobboard.dto;

import com.jobboard.entity.JobPosting;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobPostingRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private String city;
    private Boolean isRemote;
    private JobPosting.PositionType positionType;
    private String salary;
}