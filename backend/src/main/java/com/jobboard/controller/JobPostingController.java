package com.jobboard.controller;

import com.jobboard.dto.JobPostingRequest;
import com.jobboard.entity.Company;
import com.jobboard.entity.JobPosting;
import com.jobboard.service.CompanyService;
import com.jobboard.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostingController {
    
    private final JobPostingService jobPostingService;
    private final CompanyService companyService;
    
    @GetMapping
    public ResponseEntity<Page<JobPosting>> getAllJobs(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean isRemote,
            @RequestParam(required = false) String positionType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        JobPosting.PositionType posType = null;
        if (positionType != null) {
            posType = JobPosting.PositionType.valueOf(positionType);
        }
        
        Page<JobPosting> jobs = jobPostingService.getJobPostingsByFilters(city, isRemote, posType, pageable);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobById(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobPosting>> getJobsByCompany(@PathVariable Long companyId) {
        List<JobPosting> jobs = jobPostingService.getJobPostingsByCompanyId(companyId);
        return ResponseEntity.ok(jobs);
    }
    
    @PostMapping
    public ResponseEntity<?> createJob(
            @Valid @RequestBody JobPostingRequest request,
            @RequestHeader("Authorization") String token) {
        
        try {
            Company company = companyService.getCompanyById(1L)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            
            JobPosting jobPosting = new JobPosting();
            jobPosting.setCompany(company);
            jobPosting.setTitle(request.getTitle());
            jobPosting.setDescription(request.getDescription());
            jobPosting.setLocation(request.getLocation());
            jobPosting.setCity(request.getCity());
            jobPosting.setIsRemote(request.getIsRemote());
            jobPosting.setPositionType(request.getPositionType());
            jobPosting.setSalary(request.getSalary());
            
            JobPosting savedJob = jobPostingService.createJobPosting(jobPosting);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingRequest request) {
        
        try {
            JobPosting jobPosting = jobPostingService.getJobPostingById(id)
                    .orElseThrow(() -> new RuntimeException("Job posting not found"));
            
            jobPosting.setTitle(request.getTitle());
            jobPosting.setDescription(request.getDescription());
            jobPosting.setLocation(request.getLocation());
            jobPosting.setCity(request.getCity());
            jobPosting.setIsRemote(request.getIsRemote());
            jobPosting.setPositionType(request.getPositionType());
            jobPosting.setSalary(request.getSalary());
            
            JobPosting updatedJob = jobPostingService.updateJobPosting(id, jobPosting);
            return ResponseEntity.ok(updatedJob);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobPostingService.deleteJobPosting(id);
            return ResponseEntity.ok("Job posting deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}