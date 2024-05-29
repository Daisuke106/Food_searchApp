package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.HotPepperRestaurant;
import com.example.demo.repository.FindRestaurants;

public class HotPepperService {

	//デバッグ用のmainメソッド
	public static void main(String[] args) {
		FindRestaurants findRestaurants = new FindRestaurants();
		findRestaurants.setRange(3);
		findRestaurants.setNum(200);
		List<HotPepperRestaurant> restaurants = findRestaurants.find();
		System.out.println(restaurants.size());
	}

}