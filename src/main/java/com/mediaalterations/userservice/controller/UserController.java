package com.mediaalterations.userservice.controller;

import com.mediaalterations.userservice.dto.UserDto;
import com.mediaalterations.userservice.dto.UserProfileDto;
import com.mediaalterations.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UserProfileDto dto
    ) {
        return ResponseEntity.ok(userService.upsertProfile(id, dto));
    }
}

