import React, { useState, useEffect } from 'react';
// react-router-domのコンポーネントをインポート
import { Link } from 'react-router-dom';
// Yamada UIのコンポーネントをインポート
import './App.css';
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
// framer-motionのコンポーネントをインポート
// AnimatePresenceは、コンポーネントがマウントされたりアンマウントされたりするときにアニメーションを適用するために使用される
// AnimatePresenceは、motion.divコンポーネントの子要素として使用される
import { motion, AnimatePresence } from 'framer-motion';
// AnimatePageコンポーネントをインポート
// AnimatePageコンポーネントは、ページ遷移時にアニメーションを適用するために使用される
import AnimatePage from './AnimatePage';
// ImageFadeコンポーネントをインポート
// ImageFadeコンポーネントは、画像をフェードイン/フェードアウトするために使用される
import ImageFade from './ImageFade';

// ページ遷移用
import { useNavigate } from 'react-router-dom';

// ロゴ画像のパスを定義
// ロゴ画像は、public/imagesディレクトリに配置されている
const logoImage = process.env.PUBLIC_URL + '/images/FOOD_search.jpg';

// Signinコンポーネントを定義
function Signin() {
    // useLoadingフックを使用して、ローディングスクリーンを表示するための関数を取得
    const { screen } = useLoading();
    // useStateフックを使用して、ユーザーIDとパスワードの状態を管理
    const [isSubmitting, setSubmitting] = useState(false);
    // ユーザーIDの状態を管理するための変数と、ユーザーIDを更新するための関数を取得
    const [userID, setUserID] = useState('');
    // パスワードの状態を管理するための変数と、パスワードを更新するための関数を取得
    const [password, setPassword] = useState('');
    // useDisclosureフックを使用して、モーダルの表示状態を管理するための変数と、モーダルを開くための関数と閉じるための関数を取得
    const { isOpen, onOpen, onClose } = useDisclosure();
    // ページの表示状態を管理するための変数を定義
    // ページの表示状態は、ロゴ画像の表示後にtrueに設定される
    const [showFullScreenLogo, setShowFullScreenLogo] = useState(true);
    // ページのコンテンツを表示するための状態を管理するための変数を定義
    // ページのコンテンツは、ロゴ画像の表示後に表示される
    const [showPageContent, setShowPageContent] = useState(false);
    // 画像の表示順を管理するための変数を定義
    const [currentImage, setCurrentImage] = useState(0);
    // 画像のリストを定義
    const [errorMessage, setErrorMessage] = useState('');
    const images = [
        "/images/signin_img_0.png",
        "/images/signin_img_1.jpg",
        "/images/signin_img_2.jpg",
        "/images/signin_img_3.jpg",
    ];
    const navigate = useNavigate();

    // useEffectフックを使用して、コンポーネントがマウントされたときに処理を実行
    useEffect(() => {
        // ロゴ画像の表示時間を設定
        const bodyStyle = document.body.style;
        bodyStyle.overflow = 'hidden';

        // ロゴ画像の表示時間後にフェードアウトしてページコンテンツを表示
        setTimeout(() => {
            // ロゴ画像を非表示に設定
            setShowFullScreenLogo(false);
            // ページコンテンツを表示するためのタイマーを設定
            setTimeout(() => {
                // ページコンテンツを表示
                setShowPageContent(true);
                // ボディのスクロールバーを非表示に設定
                bodyStyle.overflow = 'hidden';
                // 画像の切り替えを行うためのタイマーを設定
            }, 1500);
            // 画像の切り替えを行うためのインターバルを設定
        }, 1500);

        // 左画像の切り替えを行うためのインターバルを設定
        const intervalId = setInterval(() => {
            // 画像のインデックスを更新
            setCurrentImage((prevImage) => (prevImage + 1) % images.length);
        }, 8000);

        // コンポーネントがアンマウントされたときにクリーンアップ処理を実行
        return () => {
            // インターバルをクリア
            clearInterval(intervalId);
            // ボディのスクロールバーを非表示に設定
            bodyStyle.overflow = 'hidden';
        };
    }, [images.length]);

    // ログイン処理を行うための関数を定義
    const handleLogin = async (event) => {
        // フォームのデフォルトの動作をキャンセル
        event.preventDefault();
        // ユーザーIDとパスワードが入力されているか、パスワードが6文字以上であるかをチェック
        if (!userID || !password || password.length < 6) {
            setErrorMessage('すべての必須項目を適切に入力してください。パスワードは6文字以上である必要があります。');
            onOpen();
            return;
        }
        // ログイン処理を開始
        setSubmitting(true);
        // ローディングスクリーンを表示
        screen.start();
        try {
            //サインインリクエストをPOST
            const response = await fetch('http://localhost:8080/api/auth/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: userID, password })
            });

            // ログイン成功時の処理
            if (response.ok) {
                const data = await response.json();
                localStorage.setItem("user", JSON.stringify(data));
                console.log('認証成功:', data);
                navigate('/main');
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
            // ローディングスクリーンを非表示
            setSubmitting(false);
            screen.finish();
        }
    };

    // レンダリング処理
    return (
        // AnimatePageコンポーネントでページ遷移時のアニメーションを適用
        <AnimatePage direction="right">
            {/* ページのコンテナを定義 */}
            <Box className="container" display="flex" height="100vh" alignItems="center" justifyContent="center">
                {/* フルスクリーンのロゴを表示 */}
                <AnimatePresence>
                    {showFullScreenLogo && (
                        // フルスクリーンのロゴをフェードイン/フェードアウトする
                        <motion.div
                            // ロゴ画像のアニメーション設定
                            className="full-screen-logo"
                            // ロゴ画像の初期状態
                            initial={{ opacity: 0 }}
                            // ロゴ画像の表示状態
                            animate={{ opacity: 1 }}
                            // ロゴ画像の非表示状態
                            // ロゴ画像が非表示になるときのアニメーション設定
                            exit={{ opacity: 0, transition: { duration: 1.5 } }}
                            // ロゴ画像のアニメーション設定
                            style={{
                                // ロゴ画像のスタイル設定
                                position: 'fixed',
                                // ロゴ画像の位置設定
                                top: 0,
                                left: 0,
                                // ロゴ画像のサイズ設定
                                width: '100%',
                                height: '100%',
                                // ロゴ画像の表示設定
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                // ロゴ画像の背景画像設定
                                backgroundImage: `url(${process.env.PUBLIC_URL + '/images/sample_food.jpg'})`,
                                // ロゴ画像の背景サイズ設定
                                backgroundSize: 'cover',
                                // ロゴ画像の背景位置設定
                                backgroundPosition: 'center'
                            }}
                        >
                            {/* ロゴ画像を表示 */}
                            <Image src={logoImage} alt="Company Logo" style={{ width: '20rem', height: 'auto' }} />
                        </motion.div>
                    )}
                </AnimatePresence>
                {/* ページのコンテンツを表示 */}
                {showPageContent && (
                    <>
                        {/* 左パネルに画像を表示 */}
                        <Box className="left-panel" flex="1">
                            {/* 画像をフェードイン/フェードアウトする */}
                            <ImageFade src={images[currentImage]} imageKey={currentImage} />
                        </Box>
                        {/* 右パネルにログインフォームを表示 */}
                        <Box className="right-panel" flex="1" padding="50px" display="flex" flexDirection="column" justifyContent="center" alignItems="center">
                            {/* ロゴ画像をクリックするとホーム(ログイン）ページに遷移 */}
                            <Link to="/">
                                {/* ロゴ画像の表示 */}
                                <Image src={logoImage} alt="Company Logo" marginBottom="20px" style={{ width: '10rem', height: 'auto', cursor: 'pointer' }} />
                            </Link>
                            <h1>ログイン</h1>
                            {/* ログインフォームを表示 */}
                            <form onSubmit={handleLogin}>
                                {/* ユーザーIDの入力フォームを表示 */}
                                <FormControl isRequired label="ユーザーID" errorMessage={!userID && "ユーザーIDは必須です。"}>
                                    {/* ユーザーIDの入力フォーム */}
                                    <Label>ユーザーID</Label>
                                    <Input type='text' placeholder='ユーザーID' value={userID} onChange={(e) => setUserID(e.target.value)} />
                                    {/* ユーザーIDが未入力の場合、エラーメッセージを表示 */}
                                    <HelperMessage>IDを入力してください。</HelperMessage>
                                </FormControl>
                                {/* パスワードの入力フォームを表示 */}
                                <FormControl isRequired label="パスワード" errorMessage={!password ? "パスワードは必須です。" : (password.length < 6 && "パスワードは6文字以上必要です。")}>
                                    <Label>パスワード</Label>
                                    <Input type='password' placeholder='パスワード' value={password} onChange={(e) => setPassword(e.target.value)} />
                                </FormControl>
                                {/* ログイン完了ボタンのリンク */}
                                <div className='submit-links'>
                                    <Button type='submit' isLoading={isSubmitting} loadingText="ログイン中...">ログイン</Button>
                                </div>
                                {/* パスワードリセットページへのリンク */}
                                <div className="additional-links">
                                    <Link to="/resetpass">パスワードをお忘れの方</Link>
                                    <Link to="/signup"><Button>新規登録はこちら</Button></Link>
                                </div>
                            </form>
                        </Box>
                    </>
                )}
                {/* ローディングスクリーンを表示 */}
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
