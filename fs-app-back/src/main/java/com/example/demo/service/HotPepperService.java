package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.HotPepperRestaurant;
import com.example.demo.repository.FindRestaurants;

@Service
public class HotPepperService {
	@Value("${hotpepper.gourmet.api.key}")
	private String apiKey;

	public List<HotPepperRestaurant> getNearbyRestaurants(double lat, double lng) {
		FindRestaurants findRestaurants = new FindRestaurants(apiKey);
		Map<String, String> location = Map.of(
				"lat", String.valueOf(lat),
				"lng", String.valueOf(lng));
		findRestaurants.setLocation(location);
		List<HotPepperRestaurant> restaurants = findRestaurants.find();
		return restaurants;
	}

}