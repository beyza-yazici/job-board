package com.jobboard.service;

import com.jobboard.entity.Candidate;
import com.jobboard.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class CandidateService {

    private final CandidateRepository candidateRepository;

    public Candidate createCandidate(Candidate candidate) {
        if (candidateRepository.existsByUserId(candidate.getUser().getId())){
            throw new RuntimeException("Candidate profile already exists for this user");
        }
        return candidateRepository.save(candidate);
    }

    public Optional<Candidate> getCandidateById(Long id){
        return candidateRepository.findById(id);
    }

    public Optional<Candidate> getCandidateByUserId(Long userId){
        return candidateRepository.findByUserId(userId);
    }

    public Page<Candidate> getAllCandidates(Pageable pageable) {
    return candidateRepository.findAll(pageable);
}

    public Candidate updateCandidate(Long id, Candidate updatedCandidate){
        Candidate candidate = candidateRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Candidate not found"));

        candidate.setFullName(updatedCandidate.getFullName());
        candidate.setPhone(updatedCandidate.getPhone());
        candidate.setResumeUrl(updatedCandidate.getResumeUrl());
        candidate.setSkills(updatedCandidate.getSkills());

        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
    if (!candidateRepository.existsById(id)) {
        throw new RuntimeException("Candidate not found");
    }
    candidateRepository.deleteById(id);

}