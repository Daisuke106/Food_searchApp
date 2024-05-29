package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GooglePlacesService {
	private String apiKey;

	private final String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

	public String getNearbyRestaurants(double lat, double lng) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format("%s?location=%f,%f&radius=1500&type=restaurant&key=%s", baseUrl, lat, lng, apiKey);
		return restTemplate.getForObject(url, String.class);
	}
}
