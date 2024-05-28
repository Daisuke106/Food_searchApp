package com.example.demo.entity;

import java.util.List;
import java.util.Map;

public class HotPepperRestaurant {
	private String id;
	private String name;
	private String name_kana;
	private String address;
	private String open;
	private String close;
	private String logo_url;
	private String station_name;
	private String area_name; //middle_area.name
	private Map<String, String> location;
	private String genre_name; //genre.name
	private String genre_catch;
	private String catch_word;
	private String access;
	private String mobile_access;
	private String urls;
	private List<String> photos;
	private List<String> mobile_photos;

	public HotPepperRestaurant() {
	}

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_kana() {
		return name_kana;
	}

	public void setName_kana(String name_kana) {
		this.name_kana = name_kana;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public Map<String, String> getLocation() {
		return location;
	}

	public void setLocation(Map<String, String> location) {
		this.location = location;
	}

	public String getGenre_name() {
		return genre_name;
	}

	public void setGenre_name(String genre_name) {
		this.genre_name = genre_name;
	}

	public String getGenre_catch() {
		return genre_catch;
	}

	public void setGenre_catch(String genre_catch) {
		this.genre_catch = genre_catch;
	}

	public String getCatch_word() {
		return catch_word;
	}

	public void setCatch_word(String catch_word) {
		this.catch_word = catch_word;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getMobile_access() {
		return mobile_access;
	}

	public void setMobile_access(String mobile_access) {
		this.mobile_access = mobile_access;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public List<String> getMobile_photos() {
		return mobile_photos;
	}

	public void setMobile_photos(List<String> mobile_photos) {
		this.mobile_photos = mobile_photos;
	}
}