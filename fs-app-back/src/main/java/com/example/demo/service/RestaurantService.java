package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.model.GooglePlaceRestaurant;

@Service
public class RestaurantService {

    public List<GooglePlaceRestaurant> getAllRestaurants(JSONObject jsonObj) {
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
                }
            }

            restaurant.setIcon(result.optString("icon"));
            restaurant.setName(result.optString("name"));

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

            list.add(restaurant);
        }

        return list;
    }
}
