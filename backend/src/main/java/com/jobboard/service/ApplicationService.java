package com.jobboard.service;

import com.jobboard.entity.Application;
import com.jobboard.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public Application createApplication(Application application) {

        if (applicationRepository.existsByCandidateIdAndJobPostingId(
            application.getCandidate().getId(),
            application.getJobPosting().getId())) {
            throw new RuntimeException("You have already applied for this job");
        }
        return applicationRepository.save(application);
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByJobPostingId(Long jobPostingId) {
        return applicationRepository.findByJobPostingId(jobPostingId);
    }

    public List<Application> getApplicationsByCandidateId(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public Application updateApplicationStatus(Long id, Application.ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Application not found");
        }
        applicationRepository.deleteById(id);
    }
}