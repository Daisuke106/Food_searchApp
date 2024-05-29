package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.HotPepperRestaurant;
import com.example.demo.repository.FindRestaurants;

@Service
public class HotPepperService {

	//デバッグ用のmainメソッド
	public static void main(String[] args) {
		FindRestaurants findRestaurants = new FindRestaurants();
		System.out.println(findRestaurants.getLocation());
		findRestaurants.setRange(2);
		findRestaurants.setNum(30);
		List<HotPepperRestaurant> restaurants = findRestaurants.find();
		System.out.println(restaurants.size());
		for (HotPepperRestaurant restaurant : restaurants) {
			restaurant.showDetail();
		}
	}

	public List<HotPepperRestaurant> getNearbyRestaurants(double lat, double lng) {
		FindRestaurants findRestaurants = new FindRestaurants();
		Map<String, String> location = Map.of(
				"lat", String.valueOf(lat),
				"lng", String.valueOf(lng));
		findRestaurants.setLocation(location);
		List<HotPepperRestaurant> restaurants = findRestaurants.find();
		return restaurants;
	}

}