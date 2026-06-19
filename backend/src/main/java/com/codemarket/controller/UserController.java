package com.codemarket.controller;

import com.codemarket.dto.ChangePasswordRequest;
import com.codemarket.dto.UpdateProfileRequest;
import com.codemarket.dto.UserDto;
import com.codemarket.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        UserDto profile = userService.getProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateProfile(Authentication authentication,
                                                 @Valid @RequestBody UpdateProfileRequest request) {
        UserDto profile = userService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
