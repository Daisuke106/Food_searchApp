package com.example.demo.repository;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
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

public class FindRestaurants {
	// 各初期値の設定
	//	@Value("${hotpepper.gourmet.api.key}")
	private String apiKey = "89ccfea0cc316080";
	private final OkHttpClient client = new OkHttpClient();
	private final String apiUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/";

	// 検索する件数
	private int countNum;

	// 検索範囲
	private int range;

	// 検索順序（1-3: 距離順, 4: おすすめ順）
	private int order;

	// 現在地
	private Map<String, String> location = new HashMap<>();

	private String formLat;
	private String formLng;

	/*    検索関連の値を設定    */

	// 検索する名前
	private String name_any;
	// 検索するキーワード
	private String keyword;
	// 検索する住所
	private String address;

	// 検索する大エリア
	private String large_area; // エリアマスタAPIから取得
	// 検索する中エリア
	private String middle_area; // エリアマスタAPIから取得
	// 検索する小エリア
	private String small_area; // エリアマスタAPIから取得

	// 検索するジャンル
	private String genre; // コードをマスターAPIから取得
	// 検索する予算
	private String budget; // コードをマスターAPIから取得

	// 検索URLを作成
	private String baseUrl;

	// デフォルトコンストラクタ(debug用)
	public FindRestaurants() {
		// 渋谷を初期値に
		location = new HashMap<>();
		location.put("lat", "35.6581");
		location.put("lng", "139.7017");
		formLat = String.format("%.2f", Double.parseDouble(location.get("lat")));
		formLng = String.format("%.2f", Double.parseDouble(location.get("lng")));
		countNum = 50;
		range = 1;
		order = 4;
		baseUrl = apiUrl + "?key=" + apiKey +
				"&lat=" + formLat +
				"&lng=" + formLng +
				"&range=" + range +
				"&order=" + order;
	}

	// 以降、setterと共にbaseUrlを更新

	public void setNum(int countNum) {
		this.countNum = countNum;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public Map<String, String> getLocation() {
		return location;
	}

	public void setLocation(Map<String, String> location) {
		this.location = location;
	}

	public void setLocation(String lat, String lng) {
		location.put("lat", lat);
		location.put("lng", lng);
	}

	public String getName_any() {
		return name_any;
	}

	public void setName_any(String name_any) {
		baseUrl += "&name=" + name_any;
		this.name_any = name_any;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		baseUrl += "&keyword=" + keyword;
		this.keyword = keyword;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		baseUrl += "&address=" + address;
		this.address = address;
	}

	// 検索し始めるインデックスの初期値を設定
	private int startIdx = 1;

	public List<HotPepperRestaurant> find() {
		int findCount = this.countNum;
		List<HotPepperRestaurant> restaurants = new ArrayList<>();
		boolean moreResults = true;

		while (moreResults && restaurants.size() < findCount) {
			try {
				String url = baseUrl +
						"&count=" + findCount +
						"&start=" + startIdx;
				System.out.println("POST URL: " + url);

				Request request = new Request.Builder().url(url).build();
				Response response = client.newCall(request).execute();

				if (response.isSuccessful() && response.body() != null) {
					String responseBody = response.body().string();

					// デバッグ用にレスポンスボディを出力
					System.out.println("Response Body: " + responseBody);

					// XMLレスポンスの処理
					if (responseBody.startsWith("<?xml")) {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document doc = builder.parse(new ByteArrayInputStream(responseBody.getBytes()));

						//次ページ確認処理
						String resultsAvailable = getTagValue("results_available", doc.getDocumentElement());
						String resultsReturned = getTagValue("results_returned", doc.getDocumentElement());

						System.out.println("resultsAvailable: " + resultsAvailable);

						// 各レストランの情報を取得開始
						NodeList shopNodes = doc.getElementsByTagName("shop");
						for (int i = 0; i < shopNodes.getLength(); i++) {
							if (restaurants.size() >= findCount) {
								moreResults = false;
								break;
							}
							Element shopElement = (Element) shopNodes.item(i);
							HotPepperRestaurant restaurant = new HotPepperRestaurant();
							restaurant.setOpen(getTagValue("open", shopElement));
							if (restaurant.getIsOpen()) {
								restaurant.setId(getTagValue("id", shopElement));
								restaurant.setName(getTagValue("name", shopElement));
								restaurant.setName_kana(getTagValue("name_kana", shopElement));
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
								restaurant.setUrls(getTagValue("pc",
										(Element) shopElement.getElementsByTagName("urls").item(0)));

								// 写真URLのリスト処理
								List<String> photos = new ArrayList<>();
								Element photoElement = (Element) shopElement.getElementsByTagName("photo").item(0);
								if (photoElement != null) {
									NodeList pcPhotoNodes = photoElement.getElementsByTagName("pc").item(0)
											.getChildNodes();
									for (int j = 0; j < pcPhotoNodes.getLength(); j++) {
										if (pcPhotoNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
											photos.add(pcPhotoNodes.item(j).getTextContent());
										}
									}
								}
								restaurant.setPhotos(photos);

								List<String> mobilePhotos = new ArrayList<>();
								Element mobilePhotoElement = (Element) shopElement.getElementsByTagName("mobile")
										.item(0);
								if (mobilePhotoElement != null) {
									NodeList mobilePhotoNodes = mobilePhotoElement.getChildNodes();
									for (int j = 0; j < mobilePhotoNodes.getLength(); j++) {
										if (mobilePhotoNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
											mobilePhotos.add(mobilePhotoNodes.item(j).getTextContent());
										}
									}
								}
								restaurant.setMobile_photos(mobilePhotos);

								// restrantの各情報を表示
								restaurants.add(restaurant);
								if (restaurants.size() >= findCount) {
									moreResults = false;
									break;
								}
							} // 各レストランの情報を取得終了	
						}

						// 次ページの開始インデックスを更新
						startIdx += Integer.parseInt(resultsReturned);

						// 追加の結果がない場合、ループを終了
						if (restaurants.size() >= Integer.parseInt(resultsAvailable) ||
								shopNodes.getLength() == 0) {
							moreResults = false;
						}
					}
				} else {
					System.out.println("Request failed with status code: " + response.code());
					moreResults = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				moreResults = false;
			}
		}
		return restaurants;
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
}
