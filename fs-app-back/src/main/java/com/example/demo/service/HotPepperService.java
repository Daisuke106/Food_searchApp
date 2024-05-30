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
	private String apiKey = "89ccfea0cc316080";

	public List<HotPepperRestaurant> getNearbyRestaurants(double lat, double lng) {
		FindRestaurants findRestaurants = new FindRestaurants(apiKey);
		Map<String, String> location = Map.of(
				"lat", String.valueOf(lat),
				"lng", String.valueOf(lng));
		findRestaurants.setLocation(location);
		findRestaurants.setRange(3); // 1000m
		findRestaurants.setGenreDefault();
		// "和食", "洋食", "イタリアン・フレンチ", "中華", "焼肉", "ラーメン", "お好み焼き・もんじゃ" 

		List<HotPepperRestaurant> restaurants = findRestaurants.find();
		return restaurants;
	}

	public List<HotPepperRestaurant> getNearbyRestaurantsByGenre(double lat, double lng, String genre_keyword) {
		FindRestaurants findRestaurants = new FindRestaurants(apiKey);
		Map<String, String> location = Map.of(
				"lat", String.valueOf(lat),
				"lng", String.valueOf(lng));
		findRestaurants.setLocation(location);
		findRestaurants.setRange(3); // 1000m
		findRestaurants.setGenre(genre_keyword);

		List<HotPepperRestaurant> restaurants = findRestaurants.findAll();
		return restaurants;
	}

	// デバッグ用のメインメソッド
	public static void main(String[] args) {
		HotPepperService service = new HotPepperService();
		List<HotPepperRestaurant> restaurants = service.getNearbyRestaurantsByGenre(35.681167, 139.767052, "ラーメン 洋食");
		//List<HotPepperRestaurant> restaurants = service.getNearbyRestaurants(35.681167, 139.767052);
		for (HotPepperRestaurant restaurant : restaurants) {
			System.out.println(restaurant.getName());
			System.out.println(restaurant.getGenre_name());
			System.out.println("--------------------------------------------------------------------");
		}
	}

}