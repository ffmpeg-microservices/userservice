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

        log.info("Creating user with email={}", dto.email());

        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .email(dto.email())
                .fullname(dto.fullname())
                .build();

        User saved = userRepository.save(user);

        log.info("User created with id={}", saved.getId());

        return mapToDto(saved);
    }

    public UserDto getUser(UUID userId) {
        User user = getUserOrThrow(userId);
        return mapToDto(user);
    }

    public void deleteUser(UUID userId) {

        User user = getUserOrThrow(userId);

        userRepository.delete(user);

        log.info("User deleted id={}", userId);
    }

    public UserProfileDto upsertProfile(UUID userId, UserProfileDto dto) {

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

    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFullname(),
                user.getCreatedAt()
        );
    }
}
