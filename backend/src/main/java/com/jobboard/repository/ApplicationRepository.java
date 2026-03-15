package com.jobboard.repository;

import com.jobboard.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByJobPostingId(Long jobPostingId);
    boolean existsByCandidateIdAndJobPostingId(Long candidateId, Long jobPostingId);
    Optional<Application> findByCandidateIdAndJobPostingId(Long candidateId, Long jobPostingId);
}