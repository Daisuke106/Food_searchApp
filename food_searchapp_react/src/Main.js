import React, { useState, useRef } from 'react';
import { Box, Button, Grid, GridItem, Card, CardHeader, CardBody, CardFooter } from '@yamada-ui/react';
import { LoadScript, GoogleMap } from "@react-google-maps/api";
import Slider from "react-slick";
import { useNotice } from '@yamada-ui/react';
import './Main.css'; // 更新されたCSSファイルのインポート

function Main() {
    const [position, setPosition] = useState({ lat: 35.680959106959, lng: 139.76730676352 });
    const notice = useNotice();
    const noticeRef = useRef();
    const datas = [{ url: "/images/family_food.jpg", name: "店舗1" }, { url: "/images/sample_food.jpg", name: "店舗2" }];

    const handleLocation = () => {
        noticeRef.current = notice({
            title: "位置情報取得中",
            description: "現在地を検索しています...",
            status: "loading"
        });
        navigator.geolocation.getCurrentPosition(
            (pos) => {
                const { latitude, longitude } = pos.coords;
                setPosition({ lat: latitude, lng: longitude });
                if (noticeRef.current) {
                    notice.update(noticeRef.current, {
                        title: "位置情報取得成功",
                        description: "マップが更新されました。",
                        status: "success"
                    });
                }
            },
            (err) => {
                if (noticeRef.current) {
                    notice.update(noticeRef.current, {
                        title: "位置情報取得エラー",
                        description: err.message,
                        status: "error"
                    });
                }
            }
        );
    };

    const sliderSettings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 3000,
    };

    return (
        <Box className="container-main">
            <Box className="header2">
                <div style={{ position: 'absolute', top: '10px', left: '10px', zIndex: 100 }}>
                    <img src="/images/FOOD_search.jpg" alt="FoodSearch Logo" style={{ width: '120px', height: 'auto' }} />
                </div>
                <Slider {...sliderSettings} className="slider-container">
                    {datas.map(data => (
                        <div key={data.url}>
                            <img src={data.url} alt={data.name} className="slider-image" />
                        </div>
                    ))}
                </Slider>
            </Box>
            <Box className="button-container">
                <Button onClick={handleLocation} colorScheme="blue">現在地の取得</Button>
                <Box className="map-container">
                    <LoadScript
                        googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY}
                        loadingElement={<div>Loading...</div>}
                        async={true}
                        defer={true}
                    >
                        <GoogleMap mapContainerStyle={{ width: "100%", height: "100%" }} center={position} zoom={15}>
                        </GoogleMap>
                    </LoadScript>

                </Box>
                <Box as="h2" textAlign="center" m="4">
                    周辺のおすすめ店舗
                </Box>
                <Grid templateColumns="repeat(3, 1fr)" gap={6}>
                    <GridItem>
                        <Card>
                            <CardHeader>店舗1</CardHeader>
                            <CardBody>店舗1の説明</CardBody>
                            <CardFooter>詳細情報</CardFooter>
                        </Card>
                    </GridItem>
                    <GridItem>
                        <Card>
                            <CardHeader>店舗2</CardHeader>
                            <CardBody>店舗2の説明</CardBody>
                            <CardFooter>詳細情報</CardFooter>
                        </Card>
                    </GridItem>
                    <GridItem>
                        <Card>
                            <CardHeader>店舗3</CardHeader>
                            <CardBody>店舗3の説明</CardBody>
                            <CardFooter>詳細情報</CardFooter>
                        </Card>
                    </GridItem>
                </Grid>
            </Box>
        </Box>
    );
}

export default Main;
