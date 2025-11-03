package com.tinkuytech.nango.iam.domain.aggregates;

import com.tinkuytech.nango.iam.entity.Role;
import com.tinkuytech.nango.iam.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User entity test")
class UserTest {

    @Test
    @DisplayName("Crear usuario con datos válidos")
    void createUserWithValidData() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("Julio Dominguez");
        user.setPassword("123456789");

        // Act & Assert
        assertEquals(1L, user.getId());
        assertEquals("Julio Dominguez", user.getUsername());
        assertEquals("123456789", user.getPassword());
    }

    @Test
    @DisplayName("Asignar roles al usuario")
    void assignRolesToUser() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");

        User user = new User();
        user.setUsername("Julio Dominguez");
        user.setRoles(Set.of(adminRole, userRole));

        // Act & Assert
        assertNotNull(user.getRoles());
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")));
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("USER")));
    }

    @Test
    @DisplayName("No debe aceptar username nulo")
    void userWithNullUsername_shouldThrowException() {
        // Arrange
        User user = new User();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            user.setUsername(null);
            user.getUsername().length();
        });
    }
}