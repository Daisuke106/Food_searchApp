package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	Optional<Profile> findByUserId(int userId);

	void deleteByUserId(int userId);
}