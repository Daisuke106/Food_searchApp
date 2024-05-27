package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login") // APIリクエスト(/api/auth/login)に対してのレスポンス
	public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> loginData) {
		String username = loginData.get("username");
		String password = loginData.get("password");
		System.out.println(String.format("ログインリクエスト: username=%s, password=%s", username, password));
		boolean isAuthenticated = authService.loginUser(username, password);
		if (isAuthenticated) {
			Optional<User> userOptional = authService.getUserByUsername(username);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				Map<String, String> response = new HashMap<>();
				response.put("username", username);
				response.put("userId", String.valueOf(user.getId()));
				System.out.println(String.format("認証成功：%s", username));
				return ResponseEntity.ok(response);
			} else {
				System.out.println(String.format("ユーザが見つかりません：%s", username));
				return ResponseEntity.status(404).body(null);
			}
		} else {
			System.out.println(String.format("認証失敗：%s", username));
			Map<String, String> response = new HashMap<>();
			response.put("error", "Invalid username or password");
			return ResponseEntity.status(401).body(response);
		}
	}

	@PostMapping("/register") // APIリクエスト(/api/auth/register)に対してのレスポンス
	public ResponseEntity<User> registerUser(@RequestBody Map<String, String> userMap) {
		String username = userMap.get("username");
		String email = userMap.get("email");
		String password = userMap.get("password");

		if (authService.isUserExists(username, email)) {
			return ResponseEntity.status(409).body(null);
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(authService.hashPassword(password));
		user.setEmail(email);

		User savedUser = authService.saveUser(user);
		return ResponseEntity.status(201).body(savedUser);
	}
}