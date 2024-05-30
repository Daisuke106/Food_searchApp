package com.example.demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.model.GooglePlaceRestaurant;


@Service
public class RestaurantService {
	public List<GooglePlaceRestaurant> getAllRestaurants(JSONObject jsonObj ,String sort) {
		List<GooglePlaceRestaurant> list = new ArrayList<>();

		JSONArray results = jsonObj.getJSONArray("results");

		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			GooglePlaceRestaurant restaurant = new GooglePlaceRestaurant();

			if (result.has("geometry")) {
				JSONObject geometry = result.getJSONObject("geometry");
				if (geometry.has("location")) {
					JSONObject location = geometry.getJSONObject("location");
					restaurant.setLocation(location.optString("lat"), location.optString("lng"));
					//					System.out.println("location:" + tabledata.getLocation());
				}
			}

			restaurant.setIcon(result.optString("icon"));
			restaurant.setName(result.optString("name"));

			//			System.out.println("icon:" + tabledata.getIcon());
			//			System.out.println(tabledata.getName());

			if (result.has("opening_hours")) {
				JSONObject openingHours = result.getJSONObject("opening_hours");
				restaurant.setOpen_now(String.valueOf(openingHours.optBoolean("open_now")));
			}

			restaurant.setPrice_level(result.optString("price_level"));
			restaurant.setRating(result.optString("rating"));
			restaurant.setVicinity(result.optString("vicinity"));

			if (result.has("photos")) {
				JSONArray jsonPhotos = result.getJSONArray("photos");
				List<String> photos = new ArrayList<>();
				for (int j = 0; j < jsonPhotos.length(); j++) {
					JSONObject photo = jsonPhotos.getJSONObject(j);
					photos.add(photo.optString("photo_reference"));
				}
				restaurant.setPhoto(photos);
			}
//			saveRestaurant(tabledata);
			list.add(restaurant);
			
		}
		Comparator<GooglePlaceRestaurant> compare = null;
		switch(sort) {
		case "rating": {
			compare = Comparator.comparing(GooglePlaceRestaurant::getRating).reversed();
			break;
		}
		case "name": {
			compare = Comparator.comparing(GooglePlaceRestaurant::getName);
			break;
		}
		case "icon":{
			compare = Comparator.comparing(GooglePlaceRestaurant::getIcon);
            break;		
		}
		case "price_level":{
			compare = Comparator.comparing(GooglePlaceRestaurant::getPrice_level);
			break;
		}
//		case "photo":{
//			compare = Comparator.comparing(GooglePlaceRestaurant::getPhoto);
//			break;
//		}
		case "vicinity" : {
			compare = Comparator.comparing(GooglePlaceRestaurant::getVicinity);
			break;
		}
		default :
		}
		if (compare != null) {
			list.sort(compare);
		}
		
		return list;
	}


	
	
}