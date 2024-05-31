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

	// APIから取得
	// 独自のID
	private String id;
	// 店舗名
	private String name;
	// 店舗名（かな）
	private String name_kana;
	// 住所
	private String address;
	// 営業時間(全日)
	private String open;
	// ロゴ画像URL
	private String logo_url;
	// 最寄り駅
	private String station_name;
	// 中エリア
	private String area_name; //middle_area.name
	// 店舗の緯度経度
	private Map<String, String> location = new HashMap<>();
	// ジャンル
	private String genre_name; //genre.name
	// ジャンルキャッチ
	private String genre_catch;
	// サブジャンル
	private String sub_genre_name; //sub_genre.name
	// キャッチワード
	private String catch_word;
	// アクセス
	private String access;
	// モバイルアクセス
	private String mobile_access;
	// URL
	private String urls;
	// 写真URLリスト
	private List<String> photos;
	// モバイル写真URLリスト
	private List<String> mobile_photos;

	// 個別実装
	// 営業確認用
	private boolean isOpen;
	// 各曜日の営業時間
	private Map<String, String[]> openSchedule;
	// 本日の営業時間
	private String todayOpen;

	// コンストラクタ
	public HotPepperRestaurant() {
	}

	// ゲッター、セッター
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

	public Map<String, String[]> getOpenSchedule() {
		return openSchedule;
	}

	public void setOpenSchedule(Map<String, String[]> openSchedule) {
		this.openSchedule = openSchedule;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public String getTodayOpen() {
		return todayOpen;
	}

	public String getSub_genre_name() {
		return sub_genre_name;
	}

	public void setSub_genre_name(String sub_genre_name) {
		this.sub_genre_name = sub_genre_name;
	}

	// 営業時間文字列を正規化し、各曜日と営業時間を整形、取得
	private void OpenCheck() {
		try {
			// 営業時間の文字列からL.O.などの記述を削除
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

			// 曜日と時間のペアを格納するMap
			Map<String, String[]> scheduleMap = new HashMap<>();

			// 曜日と時間のペアを分割
			String[] dayTimePairs = openHours.split(" (?=\\S+?: \\d{1,2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})");

			// 曜日と時間を分割
			for (String pair : dayTimePairs) {
				try {
					// 曜日と時間を分割
					String[] dayAndTimes = pair.split(": ");
					String[] splitDays = dayAndTimes[0].split("、");
					String[] timeRanges = dayAndTimes[1].split(" ");

					// 営業時間が2パターン以上ある場合分割して格納
					List<String> timeTempList = new ArrayList<>();
					for (String times : timeRanges) {
						String[] timeTmpAry = times.split(
								"(?<=\\d{2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})(?=\\d{2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})");
						for (String time : timeTmpAry) {
							timeTempList.add(time);
						}
					}

					// splitTimeRangesに格納されている、時間範囲を取得
					String[] splitTimeRanges = timeTempList.toArray(new String[0]);

					// splitDaysに格納されている、祝日、祝前日を削除
					List<String> daysTmpList = new ArrayList<>(Arrays.asList(splitDays));
					daysTmpList.removeIf(s -> s.equals("祝日"));
					daysTmpList.removeIf(s -> s.equals("祝前日"));
					splitDays = daysTmpList.toArray(new String[0]);

					// 曜日と時間のペアを格納
					for (String days : splitDays) {

						//  "～"が含まれている場合の処理
						if (days.contains("～")) {
							// "～"で分割
							String[] dayRange = days.split("～");
							// 開始日と終了日を取得
							String startDay = dayRange[0];
							String endDay = dayRange[1];

							// 開始日から終了日までの曜日を格納
							boolean inRange = false;
							for (String day : daysOfWeek) {
								if (day.equals(startDay)) {
									inRange = true;
								}
								if (inRange) {
									scheduleMap.put(day, splitTimeRanges);
								}
								if (day.equals(endDay)) {
									break;
								}
							}
						} else {
							// "～"が含まれていない場合
							scheduleMap.put(days, splitTimeRanges);
						}
						// すべての曜日に対して時間範囲を格納
						setOpenSchedule(scheduleMap);
					}
					// 例外処理
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("スケジュールの解析中にエラーが発生しました: " + pair);
					System.out.println("openHours: " + openHours);
				} catch (Exception e) {
					System.out.println("予期しないエラーが発生しました: " + e.getMessage());
					e.printStackTrace();
				}
			}

			// 現在の曜日に対応する時間範囲を取得してチェック
			String[] splitTimeRanges = scheduleMap.get(currentDay);

			// splitTimeRangesがnullの場合はisOpenをfalseに設定
			if (splitTimeRanges == null) {
				this.isOpen = false;
				return;
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
					e.printStackTrace();
				}
			}

			this.isOpen = isWithinSchedule;

		} catch (Exception e) {
			System.out.println("OpenCheckメソッドで予期しないエラーが発生しました: " + e.getMessage());
			e.printStackTrace();
			this.isOpen = false;
		}
	}

	// 出力確認用
	// 店舗情報の一部（名前、住所、緯度経度、曜日毎の営業時間、今日の営業時間）を表示
	public void showDetail() {
		System.out.println("name: " + name);
		System.out.println("address: " + address);
		System.out.println("location: " + location);
		for (String day : openSchedule.keySet()) {
			System.out.print(day + ": ");
			String[] timeRanges = openSchedule.get(day);
			for (int i = 0; i < timeRanges.length; i++) {
				System.out.print(timeRanges[i]);
				if (i < timeRanges.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
		System.out.println("todayOpen: " + todayOpen);
		System.out.println("---------------------------------------------------------------");
	}
}