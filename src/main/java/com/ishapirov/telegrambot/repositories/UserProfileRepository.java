package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    Optional<UserProfile> findUserProfileByUserId(Integer userId);
}
