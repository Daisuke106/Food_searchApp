package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.HotPepperRestaurant;
import com.example.demo.service.HotPepperService;

@RestController
@RequestMapping("/api/HPRestaurants")
public class HPRcontroller {

	@Autowired
	HotPepperService hotPepperService;

	@GetMapping("/nearby")
	public ResponseEntity<List<HotPepperRestaurant>> getNearbyRestaurants(
			@RequestParam("lat") double lat,
			@RequestParam("lng") double lng) {
		List<HotPepperRestaurant> restaurants = hotPepperService.getNearbyRestaurants(lat, lng);
		return ResponseEntity.ok(restaurants);
	}
}