package com.jobboard.service;

import com.jobboard.entity.User;
import com.jobboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
}

    public User updateUser(Long id, User updateUser){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(updateUser.getEmail());
        user.setPassword(updateUser.getPassword());
        user.setRole(updateUser.getRole());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
        throw new RuntimeException("User not found");
    }
    userRepository.deleteById(id);
}
}