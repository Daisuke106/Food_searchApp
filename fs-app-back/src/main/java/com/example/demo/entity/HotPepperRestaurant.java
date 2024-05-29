package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotPepperRestaurant {
	private String id;
	private String name;
	private String name_kana;
	private String address;
	private String open;
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

	private String isOpen;
	private String todayOpen;

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
		OpenCheck();
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

	public String getIsOpen() {
		return isOpen;
	}

	public String getTodayOpen() {
		return todayOpen;
	}

	private void OpenCheck() {
		String openHours = this.open.replaceAll("（.*?）", "").trim();

		// 正規表現パターンを作成し文字列を整形
		String regex = "(?<=.)(?<![、～祝前 ])(?=[月火水木金土日祝])";
		openHours = openHours.replaceAll(regex, " ");

		// 現在の曜日と時刻を取得
		LocalDateTime now = LocalDateTime.now();
		String currentDayOfWeek = now.getDayOfWeek().toString();
		LocalTime currentTime = now.toLocalTime();
		// 曜日を日本語に変換
		Map<String, String> dayOfWeekMap = new HashMap<>();
		dayOfWeekMap.put("MONDAY", "月");
		dayOfWeekMap.put("TUESDAY", "火");
		dayOfWeekMap.put("WEDNESDAY", "水");
		dayOfWeekMap.put("THURSDAY", "木");
		dayOfWeekMap.put("FRIDAY", "金");
		dayOfWeekMap.put("SATURDAY", "土");
		dayOfWeekMap.put("SUNDAY", "日");

		String currentDay = dayOfWeekMap.get(currentDayOfWeek);

		// 曜日配列の作成
		String[] daysOfWeek = { "月", "火", "水", "木", "金", "土", "日" };
		//
		//		System.out.println("openHours: " + openHours);
		// スケジュールを解析してマップに格納
		Map<String, String[]> scheduleMap = new HashMap<>();
		String[] dayTimePairs = openHours.split(" (?=\\S+?: \\d{1,2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})");
		//		for (String s : dayTimePairs) {
		//			System.out.println("Pairs: " + s);
		//		}
		for (String pair : dayTimePairs) {
			try {
				//曜日と時間を分割
				String[] dayAndTimes = pair.split(": ");
				//				System.out.println("dayAndTimes:[0]: " + dayAndTimes[0]);
				//				System.out.println("dayAndTimes:[1]: " + dayAndTimes[1]);
				//曜日が時間が2パターン以上ある場合分割して格納
				String[] splitDays = dayAndTimes[0].split("、");
				//営業時間が2パターン以上ある場合分割して格納
				String[] timeRanges = dayAndTimes[1].split(" ");

				//例外処理_1
				List<String> timeTempList = new ArrayList<>();
				for (String times : timeRanges) {
					String[] timeTmpAry = times
							.split("(?<=\\d{2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})(?=\\d{2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})");
					for (String time : timeTmpAry) {
						timeTempList.add(time);
					}
				}
				String[] splitTimeRanges = timeTempList.toArray(new String[0]);

				// splitDaysに格納されている、祝日、祝前日を削除
				// 配列をリストに変換
				List<String> daysTmpList = new ArrayList<>(Arrays.asList(splitDays));
				// リストから特定の文字列を削除
				daysTmpList.removeIf(s -> s.equals("祝日"));
				daysTmpList.removeIf(s -> s.equals("祝前日"));
				// リストを再び配列に変換
				splitDays = daysTmpList.toArray(new String[0]);

				for (String days : splitDays) {
					if (days.contains("～")) {
						//						System.out.println("days: " + days);
						String[] dayRange = days.split("～");
						String startDay = dayRange[0];
						//						System.out.println("startDay: " + startDay);
						String endDay = dayRange[1];
						//						System.out.println("endDay: " + endDay);
						boolean inRange = false;
						for (String day : daysOfWeek) {
							if (day.equals(startDay)) {
								inRange = true;
							}
							if (inRange) {
								scheduleMap.put(day, splitTimeRanges);
								//								System.out.println("put: " + day);
							}
							if (day.equals(endDay)) {
								break;
							}
						}
					} else {
						scheduleMap.put(days, splitTimeRanges);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("スケジュールの解析中にエラーが発生しました: " + pair);
				System.out.println("openHours: " + openHours);
			}
		}

		/* ----------------------------- debug用 ----------------------------- */

		// スケジュールの表示
		//	System.out.println("スケジュール:");
		//	for (Map.Entry<String, String[]> entry : scheduleMap.entrySet()) {
		//		String day = entry.getKey();
		//		String[] timeRanges = entry.getValue();
		//		System.out.print(day + ": ");
		//		for (String timeRange : timeRanges) {
		//			System.out.print(timeRange + " ");
		//		}
		//		System.out.println();
		//	}
		//	System.out.println("---------------------------------------------");

		/* ----------------------------- debug用 ----------------------------- */

		// 現在の曜日に対応する時間範囲を取得してチェック
		String[] splitTimeRanges = scheduleMap.get(currentDay);

		if (splitTimeRanges == null) {
			this.isOpen = "false";
		}

		boolean isWithinSchedule = false;
		for (String timeRange : splitTimeRanges) {
			try {
				String[] times = timeRange.split("～");
				LocalTime startTime = LocalTime.parse(times[0], DateTimeFormatter.ofPattern("H:mm"));
				LocalTime endTime;
				if (times[1].startsWith("翌")) {
					endTime = LocalTime.parse(times[1].substring(1), DateTimeFormatter.ofPattern("H:mm"));
					endTime = endTime.plusHours(24); // 翌日の時間に変換
				} else {
					endTime = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("H:mm"));
				}

				//				System.out.println("チェックする時間範囲: " + startTime + " ～ " + endTime);
				this.todayOpen = startTime.toString() + "～" + endTime.toString();

				if (startTime.isBefore(endTime)) {
					// 通常の場合
					if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
						isWithinSchedule = true;
						break;
					}
				} else {
					// 翌日にまたがる場合
					if (currentTime.isAfter(startTime) || currentTime.isBefore(endTime.minusHours(24))) {
						isWithinSchedule = true;
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("時間範囲の解析中にエラーが発生しました: " + timeRange);
			}
		}

		if (isWithinSchedule) {
			this.isOpen = "true";
		} else {
			this.isOpen = "false";
		}

	}
}