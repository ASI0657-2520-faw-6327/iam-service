package com.tinkuytech.nango.iam.application.service;

import com.tinkuytech.nango.iam.dto.RegisterRequest;
import com.tinkuytech.nango.iam.dto.AuthResponse;
import com.tinkuytech.nango.iam.entity.Role;
import com.tinkuytech.nango.iam.entity.User;
import com.tinkuytech.nango.iam.repository.RoleRepository;
import com.tinkuytech.nango.iam.repository.UserRepository;
import com.tinkuytech.nango.iam.security.JwtUtil;
import com.tinkuytech.nango.iam.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService register test")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AuthService authService;

    @Test
    @DisplayName("Registrar usuario con datos válidos")
    void registerUser_success() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Julio Dominguez", "123456789", "ADMIN");
        Role role = new Role();
        role.setName("ADMIN");

        when(userRepository.existsByUsername("Julio Dominguez")).thenReturn(false);
        when(passwordEncoder.encode("123456789")).thenReturn("encodedPass");
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtUtil.generateToken("Julio Dominguez", Set.of(role))).thenReturn("mocked-jwt-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("Julio Dominguez", response.getUsername());
        assertEquals("mocked-jwt-token", response.getToken());
    }
}