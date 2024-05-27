package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.GooglePlacesService;

@RestController
public class PlacesController {

	@Autowired
	private GooglePlacesService googlePlacesService;

	@GetMapping("/api/restaurants")
	public String getNearbyRestaurants(@RequestParam double lat, @RequestParam double lng) {
		return googlePlacesService.getNearbyRestaurants(lat, lng);
	}
}
