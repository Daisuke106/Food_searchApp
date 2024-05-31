import React, { useState, useRef, useEffect } from 'react';

import {
    Box, Button, Grid, GridItem, Card, CardHeader, CardBody, CardFooter,
    useNotice, Loading, Modal, ModalOverlay, ModalHeader, ModalCloseButton,
    ModalBody, ModalFooter, Input, useDisclosure, FormControl, Label,
    Drawer, DrawerOverlay, DrawerCloseButton, DrawerHeader, DrawerBody,
    DrawerFooter, useLoading, NativeSelect, NativeOption, Select, Option, Heading
} from '@yamada-ui/react';
import { LoadScript, GoogleMap, MarkerF } from "@react-google-maps/api";
import Slider from "react-slick";
import './Main.css';
import { Player as LottiePlayer } from '@lottiefiles/react-lottie-player';
import { motion, AnimatePresence } from 'framer-motion';
import { YearPicker } from "@yamada-ui/calendar";

import userStorage from './userStorage';

// randomでnum個取得して、取得したデータを削除
const getRandomElementsAndRemove = (arr, num) => {
    let result = [];
    let arrayCopy = [...arr];

    for (let i = 0; i < num; i++) {
        const randomIndex = Math.floor(Math.random() * arrayCopy.length);
        result.push(arrayCopy.splice(randomIndex, 1)[0]);
    }

    return result;
};


function Main() {
    const { isOpen, onOpen, onClose } = useDisclosure();
    const { isOpen: isAddModalOpen, onOpen: onAddModalOpen, onClose: onAddModalClose } = useDisclosure();
    const [shopModalOpen, setShopModalOpen] = useState(false); // State to control the shop modal
    const [profileModalOpen, setProfileModalOpen] = useState(false);
    const [position, setPosition] = useState({ lat: 35.680959106959, lng: 139.76730676352 });
    const [loading, setLoading] = useState(false);
    const [userData, setUserData] = useState(null);
    const [userId, setUserId] = useState(null);

    const openProfileModal = () => setProfileModalOpen(true);
    const closeProfileModal = () => setProfileModalOpen(false);

    // const passwordPlaceholder = "*".repeat(userData.password.length);

    useEffect(() => {
        // Automatically fade out the animation after 3 seconds
        const timer = setTimeout(() => {
            setAnimationVisible(false);
        }, 3000);
        handleLocation();
        const user = userStorage.getCurrentUser();
        if (user) {
            // user取得時の処理
            console.log(user);
            setUserData(user);
            setUserId(user.userId);
        }

        return () => clearTimeout(timer);
    }, []);


    const notice = useNotice();
    const { page } = useLoading();
    const noticeRef = useRef();

    const [animationVisible, setAnimationVisible] = useState(true);
    const [datas, setDatas] = useState([]);
    const [randomRestaurants, setRandomRestaurants] = useState([]);

    const getRestaurantData = (lat, lng) => {
        fetch(`http://localhost:8080/api/HPRestaurant/nearby?lat=${lat}&lng=${lng}`)
            .then(res => res.json())
            .then(val => {
                const randomElements = getRandomElementsAndRemove(val, 3);
                setDatas(val);
                setRandomRestaurants(randomElements);
                console.log(val);
            })
            .catch(err => console.log(err));
    }

    useEffect(() => {
        getRestaurantData(position.lat, position.lng);
    }, [position]);


    const openShopModal = () => setShopModalOpen(true);
    const closeShopModal = () => setShopModalOpen(false);

    const handleSearch = async () => {
        try {
            page.start({ message: "Searching...", duration: 2000 });
            await new Promise(resolve => setTimeout(resolve, 2000)); // Simulate fetching data
            // Here you would typically handle the transition to another screen or state
            alert("Transition to new screen or update state here.");
        } finally {
            page.finish();
        }
    };

    const categorySearch = async (category) => {
        try {
            console.log("Searching for:", category);
            page.start({ message: "Searching...", duration: 5000 });
            await new Promise(resolve => setTimeout(resolve, 5000)); // Simulate fetching data
        } finally {
            page.finish();
        }
    }

    // const bannerData = [
    //     { url: datas[0].photos[0], name: datas[0].name },
    //     { url: datas[1].photos[0], name: datas[1].name },
    //     { url: datas[2].photos[0], name: datas[2].name },
    //     { url: datas[3].photos[0], name: datas[3].name },
    //     { url: datas[4].photos[0], name: datas[4].name }
    // ];

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

    // const options = [
    //     { label: "和食", value: "cat4" },
    //     { label: "洋食", value: "cat5" },
    //     { label: "中華", value: "cat7" },
    //     // { label: "居酒屋", value: "cat1" },
    //     // { label: "ダイニングバー・バル", value: "cat2" },
    //     // { label: "創作料理", value: "cat3" },
    //     // { label: "イタリアン・フレンチ", value: "cat6" },
    //     // { label: "焼肉", value: "cat8" },
    //     // { label: "韓国料理", value: "cat9" },
    //     // { label: "アジア・エスニック料理", value: "cat10" },
    //     // { label: "各国料理", value: "cat11" },
    //     // { label: "カラオケ・パーティー", value: "cat14" },
    //     // { label: "バー・カクテル", value: "cat15" },
    //     // { label: "ラーメン", value: "cat16" },
    //     // { label: "お好み焼き・もんじゃ", value: "cat17" },
    //     // { label: "カフェ・スイーツ", value: "cat18" },
    //     // { label: "その他グルメ", value: "cat19" }
    // ];

    const sliderSettings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 3000,
    };

    const handleAddSubmit = async () => {
        setLoading(true);
        setTimeout(() => {
            setLoading(false);
            onAddModalClose(); // 追加登録のモーダルを閉じる
            alert('登録が完了しました。');
        }, 2000);
    };

    return (
        <Box className="container-main">
            <AnimatePresence>
                {animationVisible && (
                    <motion.div
                        initial={{ opacity: 1 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        transition={{ duration: 1 }}
                        className="full-screen-container"
                    >
                        <LottiePlayer
                            src="https://lottie.host/004ee795-3c08-4a26-81c0-83bdcb217796/aUF19q2JRc.json"
                            background="transparent"
                            speed={1}
                            style={{ width: '70%', height: '70%' }}
                            autoplay
                        />
                    </motion.div>
                )}
            </AnimatePresence>
            {randomRestaurants.length > 0 ? (
                <>
                    <Box className="header2">
                        <div style={{ position: 'absolute', top: '10px', left: '10px', zIndex: 100 }}>
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
                                <div key={data.photos[0]}>
                                    <img src={data.photos[0]} alt={data.name} className="slider-image" />
                                </div>
                            ))}
                        </Slider>

                    </Box>
                    <Box className="button-container">

                        <Box className="centered-container">
                            <Card className='main_card'>
                                <CardHeader>
                                    <Box
                                        display="flex"
                                        justifyContent="center"
                                        alignItems="center"
                                        width="600px"
                                        marginTop="10px" // 画像とタイトルの間に少しスペースを追加
                                        flexWrap="wrap"
                                    >
                                        <Heading as="h2" size="xl" marginBottom="10px" textAlign="center" width="100%">
                                            {datas[0].name}
                                        </Heading>
                                        <Box
                                            as="img"
                                            src={datas[0].photos[0]}
                                            alt="Example"
                                            width="300px"
                                            height="300px"
                                            objectFit="cover"
                                        />
                                    </Box>
                                </CardHeader>
                                <CardBody>
                                    <Heading as="h3" size="md" fontWeight="normal" marginBottom="8px">
                                        {`${datas[0].catch_word}`}
                                    </Heading>
                                </CardBody>
                                <CardFooter>
                                    <Box>
                                        <p>{datas[0].address}</p>
                                        <p>営業時間: {datas[0].todayOpen}</p>
                                        <p>{datas[0].catch_word}</p>
                                    </Box>
                                </CardFooter>
                            </Card>
                        </Box>

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
                                <GoogleMap mapContainerStyle={{ width: "100%", height: "100%" }} center={position} zoom={18}>
                                    <MarkerF position={position} />
                                </GoogleMap>
                            </LoadScript>
                        </Box>
                        <Box as="h1" textAlign="center" m="4">
                            周辺のおすすめ店舗
                        </Box>
                        <Grid className='grid-background' templateColumns="repeat(3, 1fr)" gap={6}>
                            {randomRestaurants.map((restaurant, index) => (
                                <GridItem key={index}>
                                    <Card className='card'>
                                        <CardHeader>
                                            <Box
                                                display="flex"
                                                justifyContent="center"
                                                alignItems="center"
                                                width="100%"
                                                marginTop="10px" // 画像とタイトルの間に少しスペースを追加
                                                flexWrap="wrap"
                                            >
                                                <Heading as="h2" size="xl" marginBottom="10px" textAlign="center" width="100%">
                                                    {restaurant.name}
                                                </Heading>
                                                <Box
                                                    as="img"
                                                    src={restaurant.photos[0]}
                                                    alt="Example"
                                                    width="300px"
                                                    height="300px"
                                                    objectFit="cover"
                                                />
                                            </Box>
                                        </CardHeader>
                                        <CardBody>
                                            <Heading as="h3" size="md" fontWeight="normal" marginBottom="8px">
                                                {`${restaurant.catch_word}`}
                                            </Heading>
                                        </CardBody>
                                        <CardFooter>
                                            <Box>
                                                <p>{restaurant.address}</p>
                                                <p>営業時間: {restaurant.todayOpen}</p>
                                                <p>{restaurant.catch_word}</p>
                                            </Box>
                                        </CardFooter>
                                    </Card>

                                    {/* 後に追記 */}
                                    {/* <NativeSelect
                                        placeholder="Select category"
                                        onChange={(e) => categorySearch(e.target.value)}
                                        defaultValue=""
                                    >
                                        <NativeOption value="">Select an option</NativeOption>
                                        {options.map(option => (
                                            <NativeOption key={option.value} value={option.value}>{option.label}</NativeOption>
                                        ))}
                                    </NativeSelect> */}
                                </GridItem>
                            ))}
                            <Grid templateColumns="repeat(2, 1fr)" gap="2" id='near-buttons'>
                                <GridItem colSpan={3} textAlign="center">
                                    <Button colorScheme="blue" w="full">もっと見る</Button>
                                </GridItem>
                                <GridItem colSpan={3} textAlign="center">
                                    <Button onClick={handleLocation} colorScheme="green" w="full">再検索</Button>
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
                                    <Button onClick={openProfileModal} colorScheme="orange">プロフィール</Button>

                                    <Modal isOpen={profileModalOpen} onClose={closeProfileModal} size="md">
                                        <ModalOverlay />
                                        <ModalCloseButton />
                                        <ModalHeader>プロフィール</ModalHeader>
                                        <ModalBody>
                                            <FormControl mt={4}>
                                                <Label>ユーザ名</Label>
                                                <Input type="text" value={userData.name} readOnly />
                                            </FormControl>
                                            <FormControl>
                                                <Label>メールアドレス</Label>
                                                <Input type="email" value={userData.email} readOnly />
                                            </FormControl>
                                            {/* <FormControl mt={4}>
                                                <Label>パスワード</Label>
                                                <Input type="text" value={passwordPlaceholder} readOnly />
                                            </FormControl> */}
                                        </ModalBody>
                                        <ModalFooter>
                                            <Button variant="ghost" onClick={closeProfileModal}>閉じる</Button>
                                        </ModalFooter>
                                    </Modal>
                                </GridItem>
                                <GridItem>
                                    <Button onClick={onAddModalOpen}>追加登録</Button>
                                    <Modal isOpen={isAddModalOpen} onClose={onAddModalClose} isCentered>
                                        <ModalOverlay />
                                        <ModalHeader>追加登録</ModalHeader>
                                        <ModalCloseButton />
                                        <ModalBody>
                                            <FormControl>
                                                <Label>性別</Label>
                                                <Select placeholder="性別を選択">
                                                    <Option value="male">男性</Option>
                                                    <Option value="female">女性</Option>
                                                    <Option value="other">その他</Option>
                                                </Select>
                                            </FormControl>
                                            <FormControl mt={4}>
                                                <Label>生年月日</Label>
                                                <YearPicker placeholder="年を選択" />
                                            </FormControl>
                                        </ModalBody>
                                        <ModalFooter>
                                            <Button colorScheme="blue" mr={3} onClick={handleAddSubmit} isLoading={loading}>
                                                送信
                                            </Button>
                                            <Button variant="ghost" onClick={onAddModalClose}>閉じる</Button>
                                        </ModalFooter>
                                    </Modal>
                                </GridItem>
                            </Grid>
                        </Box>
                    </Box>
                    <Box as="h2" textAlign="center" m="4">
                        最近の閲覧履歴
                    </Box>
                    <div className="scroll-container">
                        {datas.map(data => (
                            <div key={data.id} className="scroll-item">
                                <img src={data.photos[0]} alt={data.name} className="scroll-image" />
                                <div className="scroll-text">{data.name}</div>
                            </div>
                        ))}
                    </div>
                    <Box className="footer-container">
                        <img src="/images/FOOD_search.jpg" alt="Footer" className="footer-image" />
                    </Box>
                </>
            ) : (
                <p>Loading...</p>
            )}
        </Box>

    );
};


export default Main;
