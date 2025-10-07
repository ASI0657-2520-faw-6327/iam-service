package com.tinkuytech.nango.iam.dto;

import com.tinkuytech.nango.iam.entity.User;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {

    private String username;
    private Set<String> roles;

    public UserDTO() {}

    public UserDTO(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public static UserDTO fromEntity(User user) {
        Set<String> roleNames = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
        return new UserDTO(user.getUsername(), roleNames);
    }
}
