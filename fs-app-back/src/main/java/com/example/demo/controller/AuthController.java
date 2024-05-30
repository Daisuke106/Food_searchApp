package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

	@Autowired
	private JavaMailSender mailSender;

	private Map<String, String> pinStorage = new HashMap<>();

	@PostMapping("/signin")
	public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> loginData) {
		String username = loginData.get("name");
		String password = loginData.get("password");
		System.out.println(String.format("ログインリクエスト: username=%s, password=%s", username, password));
		boolean isAuthenticated = authService.loginUser(username, password);
		if (isAuthenticated) {
			Optional<User> userOptional = authService.getUserByUsername(username);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				Map<String, String> response = new HashMap<>();
				response.put("name", username);
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

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userMap) {
		String name = userMap.get("name");
		String email = userMap.get("email");
		String password = userMap.get("password");

		String pin = generatePin();
		pinStorage.put(email, pin);
		sendEmail(email, pin);

		if (authService.isUserExists(name, email)) {
			return ResponseEntity.status(409).body(Map.of("message", "User already exists"));
		}

		User user = new User();
		user.setName(name);
		user.setPassword(authService.hashPassword(password));
		user.setEmail(email);

		User savedUser = authService.saveUser(user);
		return ResponseEntity.status(201)
				.body(Map.of("success", true, "message", "Signup successful", "user", savedUser));
	}

	@PostMapping("/verify-pin")
	public ResponseEntity<?> verifyPin(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String pin = request.get("pin");
		if (pinStorage.containsKey(email) && pinStorage.get(email).equals(pin)) {
			pinStorage.remove(email);
			return ResponseEntity.ok(Map.of("success", true));
		} else {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "Invalid PIN"));
		}
	}

	private String generatePin() {
		Random random = new Random();
		int pin = 1000 + random.nextInt(9000);
		return String.valueOf(pin);
	}

	private void sendEmail(String to, String pin) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Your PIN Code");
		message.setText("Your PIN code is: " + pin);
		mailSender.send(message);
	}
}