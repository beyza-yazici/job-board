package com.jobboard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequest {

    @NotNull(message = "Job posting ID is required")
    private Long jobPostingId;

}