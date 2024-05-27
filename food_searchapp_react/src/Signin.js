import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
    Box,
    Button,
    Input,
    FormControl,
    Label,
    HelperMessage,
    Image,
    useLoading,
    Modal,
    ModalOverlay,
    ModalCloseButton,
    ModalHeader,
    ModalBody,
    ModalFooter,
    useDisclosure
} from '@yamada-ui/react';
import { motion, AnimatePresence } from 'framer-motion';
import AnimatePage from './AnimatePage';
import ImageFade from './ImageFade';

const logoImage = process.env.PUBLIC_URL + '/images/FOOD_search.jpg';

function Signin() {
    const navigate = useNavigate();
    const { screen } = useLoading();
    const [isSubmitting, setSubmitting] = useState(false);
    const [userID, setUserID] = useState('');
    const [password, setPassword] = useState('');
    const { isOpen, onOpen, onClose } = useDisclosure();
    const [showFullScreenLogo, setShowFullScreenLogo] = useState(true);
    const [showPageContent, setShowPageContent] = useState(false);
    const [currentImage, setCurrentImage] = useState(0);
    const [errorMessage, setErrorMessage] = useState('');
    const images = [
        "/images/food_gurume.png",
        "https://dragon-ball-official.com/assets/img/intro/intro_1.png",
        "https://dragon-ball-official.com/assets/img/intro/intro_2.png",
        "https://dragon-ball-official.com/assets/img/intro/intro_3.png",
        "https://dragon-ball-official.com/assets/img/intro/intro_4.png",
        "/images/sample_food.jpg"
    ];

    useEffect(() => {
        const bodyStyle = document.body.style;
        bodyStyle.overflow = 'hidden';

        setTimeout(() => {
            setShowFullScreenLogo(false);
            setTimeout(() => {
                setShowPageContent(true);
                bodyStyle.overflow = 'hidden';
            }, 1500);
        }, 3000);

        const intervalId = setInterval(() => {
            setCurrentImage((prevImage) => (prevImage + 1) % images.length);
        }, 8000);

        return () => {
            clearInterval(intervalId);
            bodyStyle.overflow = 'hidden';
        };
    }, [images.length]);

    const handleLogin = async (event) => {
        event.preventDefault();
        if (!userID || !password || password.length < 6) {
            setErrorMessage('すべての必須項目を適切に入力してください。パスワードは6文字以上である必要があります。');
            onOpen();
            return;
        }
        setSubmitting(true);
        screen.start();
        try {
            const response = await fetch('http://localhost:8080/api/auth/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: userID, password })
            });

            if (response.ok) {
                const data = await response.json();
                console.log('認証成功:', data);
                // ログイン成功時の処理（例：ユーザー情報の保存、リダイレクトなど）
                navigate('/dashboard'); // 例：ダッシュボードページにリダイレクト
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.error || 'ログインに失敗しました。');
                onOpen();
            }
        } catch (error) {
            console.error('ログイン中にエラーが発生しました:', error);
            setErrorMessage('ログイン中にエラーが発生しました。');
            onOpen();
        } finally {
            setSubmitting(false);
            screen.finish();
        }
    };

    return (
        <AnimatePage direction="right">
            <Box className="container" display="flex" height="100vh" alignItems="center" justifyContent="center">
                <AnimatePresence>
                    {showFullScreenLogo && (
                        <motion.div
                            className="full-screen-logo"
                            initial={{ opacity: 0 }}
                            animate={{ opacity: 1 }}
                            exit={{ opacity: 0, transition: { duration: 1.5 } }}
                            style={{
                                position: 'fixed',
                                top: 0,
                                left: 0,
                                width: '100%',
                                height: '100%',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                backgroundImage: `url(${process.env.PUBLIC_URL + '/images/sample_food.jpg'})`,
                                backgroundSize: 'cover',
                                backgroundPosition: 'center'
                            }}
                        >
                            <Image src={logoImage} alt="Company Logo" style={{ width: '20rem', height: 'auto' }} />
                        </motion.div>
                    )}
                </AnimatePresence>
                {showPageContent && (
                    <>
                        <Box className="left-panel" flex="1">
                            <ImageFade src={images[currentImage]} imageKey={currentImage} />
                        </Box>
                        <Box className="right-panel" flex="1" padding="50px" display="flex" flexDirection="column" justifyContent="center" alignItems="center">
                            <Link to="/">
                                <Image src={logoImage} alt="Company Logo" marginBottom="20px" style={{ width: '10rem', height: 'auto', cursor: 'pointer' }} />
                            </Link>
                            <h1>ログイン</h1>
                            <form onSubmit={handleLogin}>
                                <FormControl isRequired label="ユーザーID" errorMessage={!userID && "ユーザーIDは必須です。"}>
                                    <Label>ユーザーID</Label>
                                    <Input type='text' placeholder='ユーザーID' value={userID} onChange={(e) => setUserID(e.target.value)} />
                                    <HelperMessage>IDを入力してください。</HelperMessage>
                                </FormControl>
                                <FormControl isRequired label="パスワード" errorMessage={!password ? "パスワードは必須です。" : (password.length < 6 && "パスワードは6文字以上必要です。")}>
                                    <Label>パスワード</Label>
                                    <Input type='password' placeholder='パスワード' value={password} onChange={(e) => setPassword(e.target.value)} />
                                </FormControl>
                                <div className='submit-links'>
                                    <Button type='submit' isLoading={isSubmitting} loadingText="ログイン中...">ログイン</Button>
                                </div>
                                <div className="additional-links">
                                    <Link to="/resetpass">パスワードをお忘れの方</Link>
                                    <Link to="/signup"><Button>新規登録はこちら</Button></Link>
                                </div>
                            </form>
                        </Box>
                    </>
                )}
                <Modal isOpen={isOpen} onClose={onClose}>
                    <ModalOverlay />
                    <ModalCloseButton />
                    <ModalHeader>入力エラー</ModalHeader>
                    <ModalBody>{errorMessage}</ModalBody>
                    <ModalFooter>
                        <Button onClick={onClose}>閉じる</Button>
                    </ModalFooter>
                </Modal>
            </Box>
        </AnimatePage>
    );
}

export default Signin;
