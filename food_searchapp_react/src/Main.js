import React, { useState, useRef } from 'react';
import {
    Box, Button, Grid, GridItem, Card, CardHeader, CardBody, CardFooter,
    useNotice, Loading, Modal, ModalOverlay, ModalHeader, ModalCloseButton,
    ModalBody, ModalFooter, Input, useDisclosure, FormControl, Label,
    Drawer, DrawerOverlay, DrawerCloseButton, DrawerHeader, DrawerBody, DrawerFooter, useLoading
} from '@yamada-ui/react';
import { LoadScript, GoogleMap, MarkerF } from "@react-google-maps/api";
import Slider from "react-slick";
import './Main.css';

function Main() {
    const { isOpen, onOpen, onClose } = useDisclosure();
    const [shopModalOpen, setShopModalOpen] = useState(false); // State to control the shop modal
    const [position, setPosition] = useState({ lat: 35.680959106959, lng: 139.76730676352 });
    const notice = useNotice();
    const { page } = useLoading();
    const noticeRef = useRef();
    const datas = [{ url: "/images/family_food.jpg", name: "店舗1" }, { url: "/images/sample_food.jpg", name: "店舗2" }];

    const openShopModal = () => setShopModalOpen(true);
    const closeShopModal = () => setShopModalOpen(false);

    const handleSearch = async () => {
        try {
            page.start({ message: "Searching...", duration: 5000 });
            await new Promise(resolve => setTimeout(resolve, 5000)); // Simulate fetching data
            // Here you would typically handle the transition to another screen or state
            alert("Transition to new screen or update state here.");
        } finally {
            page.finish();
        }
    };

    const bannerData = [
        { url: "/images/free_food/beer.jpg", name: "バナー1" },
        { url: "/images/free_food/breadtea.jpg", name: "バナー2" },
        { url: "/images/free_food/coffee.jpg", name: "バナー3" },
        { url: "/images/free_food/hamberger.jpg", name: "バナー4" },
        { url: "/images/free_food/pancake.jpg", name: "バナー5" }
    ];

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
                    <img src="/images/FOOD_search.jpg" alt="FoodSearch Logo" onClick={(e) => { e.stopPropagation(); onOpen(); }} style={{ width: '120px', height: 'auto', cursor: 'pointer' }} />
                </div>
                <Drawer isOpen={isOpen} placement="left" onClose={onClose}>
                    <DrawerOverlay />
                    <DrawerCloseButton />
                    <DrawerHeader>Menu</DrawerHeader>
                    <DrawerBody>
                        <p>Content for the drawer can go here. Add links or other elements as needed.</p>
                    </DrawerBody>
                    <DrawerFooter>
                        <Button variant="outline" onClick={onClose}>Close</Button>
                    </DrawerFooter>
                </Drawer>
                <Slider {...sliderSettings} className="slider-container">
                    {datas.map(data => (
                        <div key={data.url}>
                            <img src={data.url} alt={data.name} className="slider-image" />
                        </div>
                    ))}
                </Slider>
            </Box>
            <Box className="button-container">
                {/* <Button onClick={handleLocation} colorScheme="blue">現在地の取得</Button> */}
                <Box className="map-container">
                    <LoadScript
                        googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY}
                        loadingElement={
                            <div style={{ height: "100%", display: "flex", justifyContent: "center", alignItems: "center" }}>
                                <Loading variant="rings" size="50xl" color="blue.500" />
                            </div>
                        }
                        async={true}
                        defer={true}
                    >
                        <GoogleMap mapContainerStyle={{ width: "100%", height: "100%" }} center={position} zoom={15}>
                            <MarkerF position={position} />
                        </GoogleMap>
                    </LoadScript>


                </Box>
                <Box as="h1" textAlign="center" m="4">
                    周辺のおすすめ店舗
                </Box>
                <Grid className='grid-background' templateColumns="repeat(3, 1fr)" gap={6}>
                    <GridItem>
                        <Card className='card'>
                            <CardHeader>店舗1</CardHeader>
                            <CardBody>店舗1の説明</CardBody>
                            <CardFooter>詳細情報</CardFooter>
                        </Card>
                    </GridItem>
                    <GridItem>
                        <Card className='card'>
                            <CardHeader>店舗2</CardHeader>
                            <CardBody>店舗2の説明</CardBody>
                            <CardFooter>詳細情報</CardFooter>
                        </Card>
                    </GridItem>
                    <GridItem>
                        <Card className='card'>
                            <CardHeader>店舗3</CardHeader>
                            <CardBody>店舗3の説明</CardBody>
                            <CardFooter>詳細情報</CardFooter>
                        </Card>
                    </GridItem>
                    <Grid templateColumns="repeat(2, 1fr)" gap="2" id='near-buttons'>
                        <GridItem colSpan={3} textAlign="center">
                            <Button colorScheme="blue" w="full">もっと見る</Button>
                        </GridItem>
                        <GridItem colSpan={3} textAlign="center">
                            <Button colorScheme="green" w="full">再検索</Button>
                        </GridItem>
                    </Grid>
                </Grid>

                <Box as="h2" textAlign="center" m="4">
                    主要機能
                </Box>
                <Box className="features-container">
                    <Grid templateColumns={{ base: "repeat(2, 1fr)", md: "repeat(4, 1fr)" }} gap="4">
                        <GridItem>
                            <Button onClick={openShopModal} colorScheme="blue" w="full">お店を探す</Button>
                            <Modal isOpen={shopModalOpen} onClose={closeShopModal} size="5xl" isCentered>
                                <ModalOverlay />
                                <ModalHeader>お店を探す</ModalHeader>
                                <ModalCloseButton />
                                <ModalBody>
                                    <Grid templateColumns="repeat(2, 1fr)" gap="6">
                                        <GridItem colSpan={1}>
                                            <FormControl>
                                                <Label>ジャンル</Label>
                                                <Input placeholder="例：和食、イタリアン" />
                                            </FormControl>
                                        </GridItem>
                                        <GridItem colSpan={1}>
                                            <FormControl>
                                                <Label>キーワード検索</Label>
                                                <Input placeholder="キーワードで検索" />
                                            </FormControl>
                                        </GridItem>
                                        <GridItem colSpan={1}>
                                            <FormControl>
                                                <Label>日付</Label>
                                                <Input placeholder="日付選択" type="date" />
                                            </FormControl>
                                        </GridItem>
                                        <GridItem colSpan={1}>
                                            <FormControl>
                                                <Label>人数</Label>
                                                <Input placeholder="人数" type="number" />
                                            </FormControl>
                                        </GridItem>
                                        <GridItem colSpan={2}>
                                            <FormControl>
                                                <Label>条件を絞る</Label>
                                                <Input placeholder="その他条件" />
                                            </FormControl>
                                        </GridItem>
                                    </Grid>
                                </ModalBody>
                                <ModalFooter>
                                <Button colorScheme="blue" mr={3} onClick={handleSearch}>検索</Button>
          <Button variant="ghost" onClick={closeShopModal}>閉じる</Button>
                                </ModalFooter>
                            </Modal>
                        </GridItem>
                        <GridItem>
                            <Button onClick={handleLocation} colorScheme="green" w="full">近くのお店を探す</Button>
                        </GridItem>
                        <GridItem>
                            <Button colorScheme="orange" w="full">プロフィール</Button>
                        </GridItem>
                        <GridItem>
                            <Button colorScheme="red" w="full">お気に入り</Button>
                        </GridItem>
                    </Grid>
                </Box>

            </Box>
            <Box as="h2" textAlign="center" m="4">
                最近の閲覧履歴
            </Box>
            <div className="scroll-container">
                {bannerData.map(data => (
                    <div key={data.url} className="scroll-item">
                        <img src={data.url} alt={data.name} className="scroll-image" />
                        <div className="scroll-text">{data.name}</div>
                    </div>
                ))}
            </div>





            <Box className="footer-container">
                <img src="/images/FOOD_search.jpg" alt="Footer" className="footer-image" />
            </Box>


        </Box>
    );
}

export default Main;
