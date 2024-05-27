package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/**").permitAll() // /restaurantsエンドポイントへのアクセスを許可
						.anyRequest().authenticated())
				.csrf(csrf -> csrf.disable()) // CSRF保護を無効化（必要に応じて）
				.formLogin(form -> form.disable()) // フォームログインを無効化
				.httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic認証を無効化

		return http.build();
	}
}
