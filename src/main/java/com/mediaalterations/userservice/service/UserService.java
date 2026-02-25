package com.mediaalterations.userservice.service;

import com.mediaalterations.userservice.dto.UserDto;
import com.mediaalterations.userservice.dto.UserProfileDto;
import com.mediaalterations.userservice.entity.User;
import com.mediaalterations.userservice.entity.UserProfile;
import com.mediaalterations.userservice.exception.UserAlreadyExistsException;
import com.mediaalterations.userservice.exception.UserNotFoundException;
import com.mediaalterations.userservice.repository.UserProfileRepository;
import com.mediaalterations.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserDto createUser(UserDto dto) {

        log.info("Creating user with email={} id={}, {}", dto.email(), dto.id(), dto);

        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        if (dto.id() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = User.builder()
                .id(dto.id())
                .email(dto.email())
                .fullname(dto.fullname())
                .build();

        // @CreationTimestamp is populated ONLY when Hibernate executes the INSERT
        // If we want to have the createdAt value immediately, we can call saveAndFlush
        // to force the insert and get the generated timestamp.
        User saved = userRepository.saveAndFlush(user);

        log.info("User created with id={}", saved.getId());

        return mapToDto(saved);
    }

    public UserDto getUser(String userId) {
        log.info("Fetching user with id={}", userId);
        User user = getUserOrThrow(userId);
        return mapToDto(user);
    }

    public UserProfileDto getUserProfile(String userId) {

        log.info("Fetching profile for user id={}", userId);

        User user = getUserOrThrow(userId);

        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            throw new UserNotFoundException("Profile not found for user id: " + userId);
        }

        return new UserProfileDto(profile.getAddress(), profile.getPhoneNo());
    }

    public void deleteUser(String userId) {

        log.info("Deleting user with id={}", userId);

        User user = getUserOrThrow(userId);

        userRepository.delete(user);

        log.info("User deleted id={}", userId);
    }

    public UserProfileDto upsertProfile(String userId, UserProfileDto dto) {

        User user = getUserOrThrow(userId);

        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            profile = UserProfile.builder()
                    .user(user)
                    .address(dto.address())
                    .phoneNo(dto.phoneNo())
                    .build();
        } else {
            profile.setAddress(dto.address());
            profile.setPhoneNo(dto.phoneNo());
        }

        user.setUserProfile(profile);

        log.info("Profile updated for user id={}", userId);

        return dto;
    }

    private User getUserOrThrow(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new UserNotFoundException("User ID is required");
        }
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFullname(),
                user.getCreatedAt());
    }
}
