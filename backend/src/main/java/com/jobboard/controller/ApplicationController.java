package com.jobboard.controller;

import com.jobboard.dto.ApplicationRequest;
import com.jobboard.entity.Application;
import com.jobboard.entity.Candidate;
import com.jobboard.entity.JobPosting;
import com.jobboard.service.ApplicationService;
import com.jobboard.service.CandidateService;
import com.jobboard.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final CandidateService candidateService;
    private final JobPostingService jobPostingService;

    @PostMapping
    public ResponseEntity<?> applyToJob(
        @Valid @RequestBody ApplicationRequest request,
        @RequestHeader("Authorization") String token) {
            try {
                        Candidate candidate = candidateService.getCandidateById(1L)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        JobPosting jobPosting = jobPostingService.getJobPostingById(request.getJobPostingId())
                .orElseThrow(() -> new RuntimeException("Job posting not found"));

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJobPosting(jobPosting);
        application.setStatus(Application.ApplicationStatus.PENDING);

        Application savedApplication = applicationService.createApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);

            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
            }

        
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<Application>> getMyApplications(
        @RequestHeader("Authorization") String token) {
           Long candidateId = 1L;

        List<Application> applications = applicationService.getApplicationsByCandidateId(candidateId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job/{jobPostingId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(
        @PathVariable Long jobPostingId) {
        List<Application> applications = applicationService.getApplicationsByJobPostingId(jobPostingId);
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        try {
            Application.ApplicationStatus newStatus = Application.ApplicationStatus.valueOf(status);
            Application updatedApplication = applicationService.updateApplicationStatus(id, newStatus);
            return ResponseEntity.ok(updatedApplication);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}