package com.example.demo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GooglePlaceRestaurant {
	
	private Map<String, String> location;
	
	private String icon;
	
	private String name;
	
	private String open_now;
	
	private List<String> photo;
	
	private String price_level;
	
	private String rating;
	
	private String vicinity;





	public Map<String, String> getLocation() {
		return location;
	}

	public void setLocation(String str_1, String str_2) {
		Map<String, String> location= new HashMap<>();
		location.put(str_1, str_2);
		this.location = location;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpen_now() {
		return open_now;
	}

	public void setOpen_now(String open_now) {
		this.open_now = open_now;
	}
	
	

	public List<String> getPhoto() {
		return photo;
	}

	public void setPhoto(List<String> photo) {
		this.photo = photo;
	}

	public String getPrice_level() {
		return price_level;
	}

	public void setPrice_level(String price_level) {
		this.price_level = price_level;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	
}