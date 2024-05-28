package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.HotPepperRestaurant;

public class HPRsearchResult {
	private List<HotPepperRestaurant> restaurants;
	private boolean nextPage;

	public HPRsearchResult(List<HotPepperRestaurant> restaurants, boolean nextPage) {
		this.restaurants = restaurants;
		this.nextPage = nextPage;
	}

	public List<HotPepperRestaurant> getRestaurants() {
		return restaurants;
	}

	public boolean isNextPage() {
		return nextPage;
	}

	public void setRestaurants(List<HotPepperRestaurant> restaurants) {
		this.restaurants = restaurants;
	}

	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}
}