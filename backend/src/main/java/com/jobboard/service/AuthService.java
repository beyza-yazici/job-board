package com.jobboard.service;

import com.jobboard.dto.AuthResponse;
import com.jobboard.dto.LoginRequest;
import com.jobboard.dto.RegisterRequest;
import com.jobboard.entity.Candidate;
import com.jobboard.entity.Company;
import com.jobboard.entity.User;
import com.jobboard.repository.CandidateRepository;
import com.jobboard.repository.CompanyRepository;
import com.jobboard.repository.UserRepository;
import com.jobboard.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional

public class AuthService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(User.Role.valueOf(request.getRole()));
        
        User savedUser = userRepository.save(user);

        Long profileId = null;

        if (savedUser.getRole() == User.Role.CANDIDATE) {
            Candidate candidate = new Candidate();
            candidate.setUser(savedUser);
            candidate.setFullName(request.getFullName());
            candidate.setPhone(request.getPhone());
            Candidate savedCandidate = candidateRepository.save(candidate);
            profileId = savedCandidate.getId();
        } else if (savedUser.getRole() == User.Role.COMPANY) {
            Company company = new Company();
            company.setUser(savedUser);
            company.setCompanyName(request.getCompanyName());
            company.setDescription(request.getDescription());
            company.setWebsite(request.getWebsite());
            Company savedCompany = companyRepository.save(company);
            profileId = savedCompany.getId();
        }

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name(), savedUser.getId());
        return new AuthResponse(token, savedUser.getEmail(), savedUser.getRole().name(), savedUser.getId(), profileId);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Long profileId = null;

        if (user.getRole() == User.Role.CANDIDATE) {
            profileId = candidateRepository.findByUserId(user.getId())
                    .map(Candidate::getId)
                    .orElse(null);
        } else if (user.getRole() == User.Role.COMPANY) {
            profileId = companyRepository.findByUserId(user.getId())
                    .map(Company::getId)
                    .orElse(null);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getId(), profileId);
    }
}