import { Box, Button } from '@yamada-ui/react';
import { LoadScript, GoogleMap } from "@react-google-maps/api";
import React, { useState, useRef } from 'react';
import { useNotice } from '@yamada-ui/react';

function Main() {
    const [datas] = useState([]);
    const [position, setPosition] = useState({ lat: 35.680959106959, lng: 139.76730676352 });
    const notice = useNotice();
    const noticeRef = useRef();

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

    const container = {
        width: "100%",
        height: "100%"
    };

    return (
        <Box className="container" display="flex" height="100vh" alignItems="center" justifyContent="center" flexWrap="wrap">
            <Button onClick={handleLocation} colorScheme="blue">現在地の取得</Button>
            <Box width="100vw" height="50vh"></Box>
            <Box width="80vw" height="50vh">
                <LoadScript googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY}>
                    <GoogleMap mapContainerStyle={container} center={position} zoom={15}>
                    </GoogleMap>
                </LoadScript>
            </Box>
            <Box display="flex" alignItems="center" justifyContent="center" flexWrap="wrap">
                {datas.map(data => (
                    <Box width="80%" display="flex" flexWrap='nowrap'>
                        <img src={data.url} alt='レストランの写真' />
                        <div>{data.name}</div>
                    </Box>
                ))}
            </Box>
        </Box>
    );
}

export default Main;
