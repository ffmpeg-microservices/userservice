package com.mediaalterations.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;


public record UserDto(

        UUID id,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(max = 150)
        String fullname,

        LocalDateTime createdAt

) {}
