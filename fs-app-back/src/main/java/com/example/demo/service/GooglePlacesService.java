package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GooglePlacesService {
	@Value("${google.places.api.key}")
	private String apiKey;

	public String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

	public String getNearbyRestaurants(double lat, double lng) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format("%s?location=%f,%f&radius=1000&type=food&key=%s&opennow=true&minprice=1&maxprice=4", baseUrl, lat, lng, apiKey);
		String str = restTemplate.getForObject(url, String.class);
		return str;
	}
}
