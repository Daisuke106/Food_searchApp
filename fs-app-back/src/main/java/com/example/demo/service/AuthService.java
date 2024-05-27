package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class AuthService {

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private UserRepository userRepository;

	//ログイン用
	public boolean loginUser(String username, String password) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isEmpty()) {
			return false;
		}
		User user = userOptional.get();
		return passwordEncoder.matches(password, user.getPassword());
	}

	//ユーザー登録用
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	//名前かemailの重複確認
	public boolean isUserExists(String username, String email) {
		return userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent();
	}

	//パスワードのハッシュ化
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
