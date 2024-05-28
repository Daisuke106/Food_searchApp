package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.demo.entity.HotPepperRestaurant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HotPepperService {
	//	@Value("${hotpepper.gourmet.api.key}")
	private String apiKey = "89ccfea0cc316080";

	private final OkHttpClient client = new OkHttpClient();

	private final String baseUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/";

	public HPRsearchResult findRestaurantsByRange(double lat, double lng, int range, int restaurant_number, int start) {
		List<HotPepperRestaurant> restaurants = new ArrayList<>();
		boolean nextPage = false;

		// double型のlatとlngを小数点以下2桁までフォーマット
		String formLat = String.format("%.2f", lat);
		String formLng = String.format("%.2f", lng);

		String url = baseUrl + "?key=" + apiKey + "&lat=" + formLat + "&lng=" + formLng + "&range=" + range
				+ "&count=" + restaurant_number + "&start=" + start;

		try {
			Request request = new Request.Builder().url(url).build();
			Response response = client.newCall(request).execute();

			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();

				// デバッグ用にレスポンスボディを出力
				//				System.out.println("Response Body: " + responseBody);

				// XMLレスポンスの処理
				if (responseBody.startsWith("<?xml")) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new ByteArrayInputStream(responseBody.getBytes()));

					NodeList shopNodes = doc.getElementsByTagName("shop");
					for (int i = 0; i < shopNodes.getLength(); i++) {
						Element shopElement = (Element) shopNodes.item(i);
						HotPepperRestaurant restaurant = new HotPepperRestaurant();

						restaurant.setId(getTagValue("id", shopElement));
						restaurant.setName(getTagValue("name", shopElement));
						restaurant.setName_kana(getTagValue("name_kana", shopElement));
						restaurant.setOpen(getTagValue("open", shopElement));
						restaurant.setClose(getTagValue("close", shopElement));
						restaurant.setLogo_url(getTagValue("logo_image", shopElement));
						restaurant.setStation_name(getTagValue("station_name", shopElement));
						restaurant.setArea_name(getTagValue("middle_area", shopElement));

						String address = getTagValue("address", shopElement);
						address = convertZenkakuToHankaku(address);
						restaurant.setAddress(address);

						Map<String, String> location = new HashMap<>();
						location.put("lat", getTagValue("lat", shopElement));
						location.put("lng", getTagValue("lng", shopElement));
						restaurant.setLocation(location);

						Element genreElement = (Element) shopElement.getElementsByTagName("genre").item(0);
						restaurant.setGenre_name(getTagValue("name", genreElement));
						restaurant.setGenre_catch(getTagValue("catch", genreElement));
						restaurant.setCatch_word(getTagValue("catch", shopElement));
						restaurant.setAccess(getTagValue("access", shopElement));
						restaurant.setMobile_access(getTagValue("mobile_access", shopElement));
						restaurant
								.setUrls(getTagValue("pc", (Element) shopElement.getElementsByTagName("urls").item(0)));

						// 写真URLのリスト処理
						List<String> photos = new ArrayList<>();
						Element photoElement = (Element) shopElement.getElementsByTagName("photo").item(0);
						if (photoElement != null) {
							NodeList pcPhotoNodes = photoElement.getElementsByTagName("pc").item(0).getChildNodes();
							for (int j = 0; j < pcPhotoNodes.getLength(); j++) {
								if (pcPhotoNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
									photos.add(pcPhotoNodes.item(j).getTextContent());
								}
							}
						}
						restaurant.setPhotos(photos);

						List<String> mobilePhotos = new ArrayList<>();
						Element mobilePhotoElement = (Element) shopElement.getElementsByTagName("mobile").item(0);
						if (mobilePhotoElement != null) {
							NodeList mobilePhotoNodes = mobilePhotoElement.getChildNodes();
							for (int j = 0; j < mobilePhotoNodes.getLength(); j++) {
								if (mobilePhotoNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
									mobilePhotos.add(mobilePhotoNodes.item(j).getTextContent());
								}
							}
						}
						restaurant.setMobile_photos(mobilePhotos);

						restaurants.add(restaurant);
					}

					//次ページ確認処理
					String resultsAvailable = getTagValue("results_available", doc.getDocumentElement());
					String resultsReturned = getTagValue("results_returned", doc.getDocumentElement());
					System.out.println("resultsAvailable: " + resultsAvailable);
					if (resultsAvailable != null && resultsReturned != null) {
						int resultsAvailableInt = Integer.parseInt(resultsAvailable);
						int resultsReturnedInt = Integer.parseInt(resultsReturned);
						nextPage = resultsAvailableInt > resultsReturnedInt;
					}
				}

			} else {
				System.out.println("Request failed with status code: " + response.code());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new HPRsearchResult(restaurants, nextPage);
	}

	private String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag);
		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		}
		return null;
	}

	private String convertZenkakuToHankaku(String input) {
		return input.replaceAll("０", "0")
				.replaceAll("１", "1")
				.replaceAll("２", "2")
				.replaceAll("３", "3")
				.replaceAll("４", "4")
				.replaceAll("５", "5")
				.replaceAll("６", "6")
				.replaceAll("７", "7")
				.replaceAll("８", "8")
				.replaceAll("９", "9");
	}

	// debug用
	public static void main(String[] args) {
		HotPepperService api = new HotPepperService();
		HPRsearchResult findRestaurantsByRange = api.findRestaurantsByRange(35.658034, 139.701636, 1, 1, 1);
		List<HotPepperRestaurant> restaurants = findRestaurantsByRange.getRestaurants();
		for (HotPepperRestaurant restaurant : restaurants) {
			//			String openHours = restaurant.getOpen();
			String openHours = "月～土: 11:30～15:30 （料理L.O. 15:00 ドリンクL.O. 15:00）17:00～22:00 （料理L.O. 21:00 ドリンクL.O. 21:30）日、祝日: 11:30～15:30 （料理L.O. 15:00 ドリンクL.O. 15:00）17:00～21:00 （料理L.O. 20:00 ドリンクL.O. 20:30）祝前日: 11:30～15:00 （料理L.O. 15:00 ドリンクL.O. 15:00）17:00～22:00 （料理L.O. 21:00 ドリンクL.O. 21:30）";
			System.out.println("営業時間: " + openHours);
			System.out.println("Open Check: " + isCheckOpen(openHours));
			System.out.println("---------------------------------------------------------------------------------");
		}
	}

	private static String removeLOInfo(String openHours) {
		// かっこ内の情報を取り除く
		return openHours.replaceAll("（.*?）", "").trim();
	}

	private static String preprocessOpenHours(String openHours) {
		// 曜日の前に空白を挿入する
		String[] days = { "月", "火", "水", "木", "金", "土", "日" };
		for (String day : days) {
			// 曜日の前の文字が "～", "、", " ", "祝", "前" でない場合に空白を挿入
			// 最初の文字以外に適用
			openHours = openHours.replaceAll("(?<=[^～、\\s祝前])" + day, " " + day);
		}
		// 祝日と祝前日を無視する
		openHours = openHours.replaceAll("祝日", "").replaceAll("祝前日", "");
		return openHours;
	}

	/**
	 * 営業時間が現在の時刻に対応しているかを判定するメソッド
	 * @param openHours 営業時間の文字列
	 * @return 営業時間内であればtrue、そうでなければfalse
	 */
	public static boolean isCheckOpen(String openHours) {
		openHours = openHours.replaceAll("（.*?）", "").trim();
		// 正規表現パターンを作成
		String regex = "(?<=.)(?<![、～祝前 ])(?=[月火水木金土日祝])";
		openHours = openHours.replaceAll(regex, " ");

		// 現在の曜日と時刻を取得
		LocalDateTime now = LocalDateTime.now();
		String currentDayOfWeek = now.getDayOfWeek().toString();
		LocalTime currentTime = now.toLocalTime();
		// 曜日を日本語に変換
		// 祝日、祝前日の処理は値を受け取りつつほぼ無視
		Map<String, String> dayOfWeekMap = new HashMap<>();
		dayOfWeekMap.put("MONDAY", "月");
		dayOfWeekMap.put("TUESDAY", "火");
		dayOfWeekMap.put("WEDNESDAY", "水");
		dayOfWeekMap.put("THURSDAY", "木");
		dayOfWeekMap.put("FRIDAY", "金");
		dayOfWeekMap.put("SATURDAY", "土");
		dayOfWeekMap.put("SUNDAY", "日");
		dayOfWeekMap.put("HOLIDAY", "祝日");
		dayOfWeekMap.put("BEFOREHOLIDAY", "祝前日");

		String currentDay = dayOfWeekMap.get(currentDayOfWeek);

		// 曜日配列の作成
		String[] daysOfWeek = { "月", "火", "水", "木", "金", "土", "日" };

		System.out.println("openHours: " + openHours);
		// スケジュールを解析してマップに格納
		Map<String, String[]> scheduleMap = new HashMap<>();
		String[] dayTimePairs = openHours.split(" (?=\\S+?: \\d{1,2}:\\d{2}～(?:翌)?\\d{1,2}:\\d{2})");
		for (String s : dayTimePairs) {
			System.out.println("Pairs: " + s);
		}
		for (String pair : dayTimePairs) {
			try {
				//曜日と時間を分割
				String[] dayAndTimes = pair.split(": ");
				System.out.println("dayAndTimes:[0]: " + dayAndTimes[0]);
				System.out.println("dayAndTimes:[1]: " + dayAndTimes[1]);
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
						System.out.println("days: " + days);
						String[] dayRange = days.split("～");
						String startDay = dayRange[0];
						System.out.println("startDay: " + startDay);
						String endDay = dayRange[1];
						System.out.println("endDay: " + endDay);
						boolean inRange = false;
						for (String day : daysOfWeek) {
							if (day.equals(startDay)) {
								inRange = true;
							}
							if (inRange) {
								scheduleMap.put(day, splitTimeRanges);
								System.out.println("put: " + day);
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

		// スケジュールの表示
		System.out.println("スケジュール:");
		for (Map.Entry<String, String[]> entry : scheduleMap.entrySet()) {
			String day = entry.getKey();
			String[] timeRanges = entry.getValue();
			System.out.print(day + ": ");
			for (String timeRange : timeRanges) {
				System.out.print(timeRange + " ");
			}
			System.out.println();
		}
		System.out.println("---------------------------------------------------------------------------------");

		// 現在の曜日に対応する時間範囲を取得してチェック
		String[] splitTimeRanges = scheduleMap.get(currentDay);

		if (splitTimeRanges == null) {
			// デバッグ情報
			return false;
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

				System.out.println("チェックする時間範囲: " + startTime + " ～ " + endTime);

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

		return isWithinSchedule;
	}

}