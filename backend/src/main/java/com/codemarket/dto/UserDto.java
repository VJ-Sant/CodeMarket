package com.codemarket.dto;

import com.codemarket.entity.Gender;
import com.codemarket.entity.Role;
import com.codemarket.entity.User;

public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private Gender gender;
    private Role role;
    private boolean enabled;

    public UserDto() {
    }

    public UserDto(Long id, String username, String email, String phone, Gender gender, Role role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.role = role;
        this.enabled = enabled;
    }

    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getGender(),
                user.getRole(),
                user.isEnabled()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
