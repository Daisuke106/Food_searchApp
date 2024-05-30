package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;

@Service
public class ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	public Profile saveProfile(Profile profile) {
		return profileRepository.save(profile);
	}

	public Profile getProfile(int userId) {
		return profileRepository.findByUserId(userId).orElse(null);
	}

	public Profile updateProfile(Profile profile) {
		return profileRepository.save(profile);
	}

	public void deleteProfile(int userId) {
		profileRepository.deleteByUserId(userId);
	}
}