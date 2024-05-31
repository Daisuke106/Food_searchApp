package com.example.demo.repository;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.demo.entity.HotPepperRestaurant;
import com.example.demo.entity.Profile;
import com.example.demo.service.ProfileService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindRestaurants {
	// 各初期値の設定
	private final OkHttpClient client = new OkHttpClient();
	private final String apiUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/";

	private String apiKey;

	// 検索する件数
	private int countNum;

	public int getCountNum() {
		return countNum;
	}

	// 検索範囲
	private int range;

	public int getRange() {
		return range;
	}

	// 検索順序（1-3: 距離順, 4: おすすめ順）
	private int order;

	public int getOrder() {
		return order;
	}

	// 現在地
	private Map<String, String> location = new HashMap<>();

	public Map<String, String> getLocation() {
		return location;
	}

	private String formLat;
	private String formLng;

	/*    検索関連の値を設定    */

	// 検索する名前
	private String name_any;

	public String getName_any() {
		return name_any;
	}

	// 検索するキーワード
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	// 検索する住所
	private String address;

	public String getAddress() {
		return address;
	}

	// 検索する大エリア
	private String large_area; // エリアマスタAPIから取得
	// 検索する中エリア
	private String middle_area; // エリアマスタAPIから取得
	// 検索する小エリア
	private String small_area; // エリアマスタAPIから取得

	// 検索するジャンル
	private String genre; // コードをマスターAPIから取得

	public String getGenre() {
		return genre;
	}

	private String removeGenre;

	// 検索する予算
	private String budget; // コードをマスターAPIから取得

	public String getbudget() {
		return budget;
	}

	// 検索URLを作成
	private String baseUrl;

	private void addUrl(String key, String value) {
		baseUrl += "&" + key + "=" + value;
	}

	@Autowired
	ProfileService profileService;

	public void setValueByProfile(int userId) {
		Profile userProfile = profileService.getProfile(userId);

		if (userProfile != null) {
			int userRange = userProfile.getActivityRange();

			if (userRange <= 3) {
				setRange(1);
			} else if (userRange <= 5) {
				setRange(2);
			} else if (userRange <= 10) {
				setRange(3);
			} else if (userRange <= 20) {
				setRange(4);
			} else {
				setRange(5);
			}

			setGenre(userProfile.getFavoriteGenre());
			setBudget(userProfile.getBudget());
		}
	}

	// デフォルトコンストラクタ
	public FindRestaurants(String apiKey) {
		this.apiKey = apiKey;
		// 渋谷を初期値に設定
		location = new HashMap<>();
		location.put("lat", "35.6581");
		location.put("lng", "139.7017");
		formLat = String.format("%.2f", Double.parseDouble(location.get("lat")));
		formLng = String.format("%.2f", Double.parseDouble(location.get("lng")));
		countNum = 10;
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

	public void setRange(int range) {
		this.range = range;
	}

	public void setLocation(Map<String, String> location) {
		this.location = location;
	}

	public void setLocation(String lat, String lng) {
		location.put("lat", lat);
		location.put("lng", lng);
	}

	public void setName_any(String name_any) {
		baseUrl += "&name=" + name_any;
		this.name_any = name_any;
	}

	public void setKeyword(String keyword) {
		addUrl("keyword", keyword);
		this.keyword = keyword;
	}

	public void setAddress(String address) {
		baseUrl += "&address=" + address;
		this.address = address;
	}

	// ジャンルを取得する内部メソッド
	public void setGenre(String keyword) {
		String genreUrl = "http://webservice.recruit.co.jp/hotpepper/genre/v1/?key=" + apiKey;
		this.genre = keyword;
		String[] keywords = keyword.split(" ");
		List<String> genreCodes = new ArrayList<>();

		try {
			for (String key : keywords) {
				String urlWithKeyword = genreUrl + "&keyword=" + key;
				Request request = new Request.Builder().url(urlWithKeyword).build();
				Response response = client.newCall(request).execute();
				if (response.isSuccessful() && response.body() != null) {
					String responseBody = response.body().string();
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new ByteArrayInputStream(responseBody.getBytes()));
					NodeList genreNodes = doc.getElementsByTagName("genre");
					if (genreNodes.getLength() > 0) {
						Element genreElement = (Element) genreNodes.item(0);
						String genreCode = genreElement.getElementsByTagName("code").item(0).getTextContent();
						genreCodes.add(genreCode);
					}
				}
			}

			if (!genreCodes.isEmpty()) {
				for (String code : genreCodes) {
					// baseUrlにジャンルフィルターを追加
					addUrl("genre", code);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 複数ジャンルを取得する内部メソッド
	public void setGenre(String keyword[]) {
		this.genre = String.join(" ", keyword);
		String genreUrl = "http://webservice.recruit.co.jp/hotpepper/genre/v1/?key=" + apiKey;
		for (String word : keyword) {
			genreUrl += "&keyword=" + word;
		}
		try {
			Request request = new Request.Builder().url(genreUrl).build();
			Response response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new ByteArrayInputStream(responseBody.getBytes()));
				NodeList genreNodes = doc.getElementsByTagName("genre");
				for (int i = 0; i < genreNodes.getLength(); i++) {
					Element genreElement = (Element) genreNodes.item(i);
					String genreCode = genreElement.getElementsByTagName("code").item(0).getTextContent();
					// baseUrlにジャンルフィルターを追加
					addUrl("genre", genreCode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 一般的なジャンルをセット
	public void setGenreDefault() {
		String[] genreList = { "和食", "洋食", "イタリアン・フレンチ", "中華", "焼肉",
				"ラーメン", "お好み焼き・もんじゃ" };
		setGenre(genreList);
	}

	// 予算からフィルター設定する内部メソッド
	// 設定できる予算コードが2つなため、budgetを最大とした2つの予算コードを追加する仕様を仮実装
	public void setBudget(int budget) {
		this.budget = Integer.toString(budget);
		// 予算コードのマップ
		Map<String, String> budgetMap = new HashMap<>();
		budgetMap.put("0-500", "B009");
		budgetMap.put("501-1000", "B010");
		budgetMap.put("1001-1500", "B011");
		budgetMap.put("1501-2000", "B001");
		budgetMap.put("2001-3000", "B002");
		budgetMap.put("3001-4000", "B003");
		budgetMap.put("4001-5000", "B008");
		budgetMap.put("5001-7000", "B004");
		budgetMap.put("7001-10000", "B005");
		budgetMap.put("10001-15000", "B006");
		budgetMap.put("15001-20000", "B012");
		budgetMap.put("20001-30000", "B013");
		budgetMap.put("30001-", "B014");

		// 前のコードが存在するかのチェック用
		String preCode = null;
		for (Map.Entry<String, String> entry : budgetMap.entrySet()) {
			String[] range = entry.getKey().split("-");
			int lower = range[0].isEmpty() ? 0 : Integer.parseInt(range[0]);
			int upper = range.length > 1 && !range[1].isEmpty() ? Integer.parseInt(range[1]) : Integer.MAX_VALUE;

			if (budget >= lower && budget <= upper) {
				String code = entry.getValue();
				addUrl("budget", code);
				if (preCode != null) {
					addUrl("budget", preCode);
				}
				break; // マッチした場合はループを抜ける
			}
			preCode = entry.getValue();
		}
	}

	// タグの値を取得する内部メソッド
	private String getTagValue(String tag, Element element) {
		if (element != null) {
			NodeList nodeList = element.getElementsByTagName(tag);
			if (nodeList.getLength() > 0) {
				Node node = nodeList.item(0);
				if (node != null) {
					return node.getTextContent();
				}
			}
		}
		return null;
	}

	// 全角数字を半角数字に変換する内部メソッド
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

	// メインとなる検索メソッド
	// 営業中の店を検索
	public List<HotPepperRestaurant> find() {
		int findCount = this.countNum;
		int startIdx = 1;
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
								// ジャンル情報を取得
								Element genreElement = (Element) shopElement.getElementsByTagName("genre").item(0);
								restaurant.setGenre_name(getTagValue("name", genreElement));
								if (removeGenre != null && removeGenre.equals(restaurant.getGenre_name())) {
									continue;
								}
								restaurant.setGenre_catch(getTagValue("catch", genreElement));
								Element subGenreElement = (Element) shopElement.getElementsByTagName("sub_genre")
										.item(0);
								restaurant.setSub_genre_name(getTagValue("name", subGenreElement));
								if (removeGenre != null && removeGenre.equals(restaurant.getSub_genre_name())) {
									continue;
								}
								restaurant.setCatch_word(getTagValue("catch", shopElement));
								restaurant.setAccess(getTagValue("access", shopElement));
								restaurant.setMobile_access(getTagValue("mobile_access", shopElement));
								restaurant.setUrls(getTagValue("pc",
										(Element) shopElement.getElementsByTagName("urls").item(0)));
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

	// 営業時刻を管理しない全検索メソッド
	public List<HotPepperRestaurant> findAll() {
		int findCount = this.countNum;
		int startIdx = 1;
		List<HotPepperRestaurant> restaurants = new ArrayList<>();
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
					System.out.println("resultsAvailable: " + resultsAvailable);

					// 各レストランの情報を取得開始
					NodeList shopNodes = doc.getElementsByTagName("shop");
					for (int i = 0; i < shopNodes.getLength(); i++) {

						Element shopElement = (Element) shopNodes.item(i);
						HotPepperRestaurant restaurant = new HotPepperRestaurant();

						restaurant.setOpen(getTagValue("open", shopElement));
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

						// Nullチェックを追加
						Element genreElement = (Element) shopElement.getElementsByTagName("genre").item(0);
						if (genreElement != null) {
							restaurant.setGenre_name(getTagValue("name", genreElement));
							restaurant.setGenre_catch(getTagValue("catch", genreElement));
						}

						Element subGenreElement = (Element) shopElement.getElementsByTagName("sub_genre").item(0);
						if (subGenreElement != null) {
							restaurant.setSub_genre_name(getTagValue("name", subGenreElement));
						}

						if (removeGenre != null && (removeGenre.equals(restaurant.getSub_genre_name())
								|| removeGenre.equals(restaurant.getGenre_name()))) {
							continue;
						}

						restaurant.setCatch_word(getTagValue("catch", shopElement));
						restaurant.setAccess(getTagValue("access", shopElement));

						Element urlsElement = (Element) shopElement.getElementsByTagName("urls").item(0);
						if (urlsElement != null) {
							restaurant.setUrls(getTagValue("pc", urlsElement));
						}

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
						restaurants.add(restaurant);
					}

				}
			} else {
				System.out.println("Request failed with status code: " + response.code());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restaurants;
	}

	public String getRemoveGenre() {
		return removeGenre;
	}

	public void setRemoveGenre(String removeGenre) {
		this.removeGenre = removeGenre;
	}

}
