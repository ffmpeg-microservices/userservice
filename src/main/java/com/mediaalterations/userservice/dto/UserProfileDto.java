package com.mediaalterations.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record UserProfileDto(

        @NotBlank
        @Size(max = 100)
        String address,

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
        String phoneNo

) {}
