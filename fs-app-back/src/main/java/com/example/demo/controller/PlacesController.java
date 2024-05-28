package com.example.demo.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.GooglePlaceRestaurant;
import com.example.demo.service.GooglePlacesService;
import com.example.demo.service.RestaurantService;

@RestController
public class PlacesController {

    @Autowired
    private GooglePlacesService googlePlacesService;

    @GetMapping("/api/restaurants")
    public String getNearbyRestaurants(@RequestParam double lat, @RequestParam double lng) {
        return googlePlacesService.getNearbyRestaurants(lat, lng);
    }

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/api/restaurants/all")
    public List<GooglePlaceRestaurant> getParsedNearbyRestaurants(@RequestParam("lat") double lat, @RequestParam("lng") double lng) throws Exception {
        String response = googlePlacesService.getNearbyRestaurants(lat, lng);
        JSONObject jsonObj = new JSONObject(response);
        return restaurantService.getAllRestaurants(jsonObj);
    }
}
