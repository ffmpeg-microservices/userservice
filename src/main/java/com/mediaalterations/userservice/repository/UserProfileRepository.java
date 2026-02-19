package com.mediaalterations.userservice.repository;

import com.mediaalterations.userservice.entity.User;
import com.mediaalterations.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, User> {
}
