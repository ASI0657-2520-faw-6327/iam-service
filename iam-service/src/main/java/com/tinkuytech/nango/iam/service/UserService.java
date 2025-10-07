package com.tinkuytech.nango.iam.service;

import com.tinkuytech.nango.iam.dto.UserDTO;
import com.tinkuytech.nango.iam.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDTO::fromEntity);
    }
    
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
