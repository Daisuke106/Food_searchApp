import React, { useState, useRef } from 'react';
import { Box, Button } from '@yamada-ui/react';
import { LoadScript, GoogleMap } from "@react-google-maps/api";
import Slider from "react-slick";
import { useNotice } from '@yamada-ui/react';

function Main() {
    const [position, setPosition] = useState({ lat: 35.680959106959, lng: 139.76730676352 });
    const notice = useNotice();
    const noticeRef = useRef();
    // 画像パスをルートからの相対パスに変更
    const datas = [{ url: "/images/family_food.jpg", name: "店舗1" }, { url: "/images/image2.jpg", name: "店舗2" }];

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
        slidesToScroll: 1
    };

    return (
        <Box className="container" display="flex" flexDirection="column" height="100vh">
            <Box className="header" width="100%" zIndex="1000" position="fixed" top="0" backgroundColor="white">
                <Slider {...sliderSettings}>
                    {datas.map(data => (
                        <div key={data.url}>
                            <img src={data.url} alt={data.name} style={{ width: "100%", height: "auto" }} />
                        </div>
                    ))}
                </Slider>
            </Box>
            <Box display="flex" flexDirection="column" height="100vh" paddingTop="300px">
                <Button onClick={handleLocation} colorScheme="blue">現在地の取得</Button>
                <Box width="100vw" height="50vh" marginTop="20px">
                    <LoadScript googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY}>
                        <GoogleMap mapContainerStyle={{ width: "100%", height: "100%" }} center={position} zoom={15}>
                        </GoogleMap>
                    </LoadScript>
                </Box>
            </Box>
        </Box>
    );
}

export default Main;
