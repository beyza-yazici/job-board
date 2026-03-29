package com.jobboard.service;

import com.jobboard.entity.JobPosting;
import com.jobboard.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPosting createJobPosting(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public Optional<JobPosting> getJobPostingById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public List<JobPosting> getJobPostingsByCompanyId(Long companyId) {
        return jobPostingRepository.findByCompanyId(companyId);
    }

    public Page<JobPosting> getJobPostingsByFilters(
        String city,
        Boolean isRemote,
        JobPosting.PositionType positionType,
        Pageable pageable
    ){
        return jobPostingRepository.findByFilters(city, isRemote, positionType, pageable);
    }

    public JobPosting updateJobPosting(Long id, JobPosting updatedJobPosting){
        JobPosting jobPosting = jobPostingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Job posting not found"));

        jobPosting.setTitle(updatedJobPosting.getTitle());
        jobPosting.setDescription(updatedJobPosting.getDescription());
        jobPosting.setLocation(updatedJobPosting.getLocation());
        jobPosting.setCity(updatedJobPosting.getCity());
        jobPosting.setIsRemote(updatedJobPosting.getIsRemote());
        jobPosting.setPositionType(updatedJobPosting.getPositionType());
        jobPosting.setSalary(updatedJobPosting.getSalary());

        return jobPostingRepository.save(jobPosting);
    }

    public void deleteJobPosting(Long id) {
    if (!jobPostingRepository.existsById(id)) {
        throw new RuntimeException("Job posting not found");
    }
    jobPostingRepository.deleteById(id);
}
}