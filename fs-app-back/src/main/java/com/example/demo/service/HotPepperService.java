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
		findRestaurants.setRange(3); // 1000m
		findRestaurants.setNum(50);
		findRestaurants.setRemoveGenre("居酒屋");
		findRestaurants.setGenreDefault();
		// { "和食", "洋食", "イタリアン・フレンチ", "中華", "焼肉", "ラーメン", "お好み焼き・もんじゃ" }

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
		// ジャンルキーワードを設定
		// 複数の場合、文字列配列で与える他半角スペースで区切っても検索可能

		List<HotPepperRestaurant> restaurants = findRestaurants.findAll();
		return restaurants;
	}

}