// ReactのuseStateフックをインポート
import React, { useState } from 'react';
// React RouterのLinkコンポーネントとuseNavigateフックをインポート
import { Link, useNavigate } from 'react-router-dom';
// Yamada UIのコンポーネントをインポート
import {
    Box,
    Button,
    Input,
    FormControl,
    Modal,
    ModalOverlay,
    ModalHeader,
    ModalBody,
    ModalFooter,
    useLoading,
    useAnimation,
    useNotice,
    useDisclosure,
    PinInput,
    PinInputField
} from '@yamada-ui/react';

// ロゴ画像のパスを定義
const logoImage = process.env.PUBLIC_URL + '/images/FOOD_search.jpg';

// ResetPassコンポーネントを定義
function ResetPass() {
    // useNavigateフックを使用して、ナビゲーション関数を取得
    const navigate = useNavigate();
    // useLoadingフックを使用して、ローディングスクリーンを表示するための関数を取得
    const { page } = useLoading();
    // useNoticeフックを使用して、通知を表示するための関数を取得
    const notice = useNotice({
        // 通知のタイトルと説明を指定
        component: ({ description }) => (
            // 通知のスタイルを指定
            // 背景色を紫色に設定
            // パディングを3に設定
            // マージンを4に設定
            // テキストカラーを白色に設定
            <Box color="white" py={3} px={4} bg="purple.500">
                {description}
            </Box>
        ),
        // 通知のスタイルを指定
        style: { maxW: "100%", minW: "100%" },
        // 通知の表示時間を指定
        duration: 10000
    });

    // useDisclosureフックを使用して、モーダルの表示状態を管理するための変数と、モーダルを開くための関数と閉じるための関数を取得
    const { isOpen, onOpen, onClose } = useDisclosure();
    // useDisclosureフックを使用して、確認モーダルの表示状態を管理するための変数と、確認モーダルを開くための関数と閉じるための関数を取得
    const { isOpen: isConfirmOpen, onOpen: onConfirmOpen, onClose: onConfirmClose } = useDisclosure();
    // useStateフックを使用して、メールアドレスの状態を管理するための変数と、メールアドレスを更新するための関数を取得
    const [email, setEmail] = useState('');

    // useAnimationフックを使用して、アニメーションを作成するための関数を取得
    const colorChangeAnimation = useAnimation({
        // キーフレームを指定
        keyframes: {
            // 0%の位置で背景色を青色に設定
            '0%': { bg: 'blue.500' },
            '25%': { bg: 'green.500' },
            '50%': { bg: 'yellow.500' },
            '75%': { bg: 'red.500' },
            // 100%の位置で背景色を青色に設定
            '100%': { bg: 'blue.500' },
        },
        // アニメーションのプロパティを指定
        // アニメーションの継続時間を10秒に設定
        duration: '10s',
        // アニメーションの繰り返し回数を無限に設定
        iterationCount: 'infinite',
        // アニメーションのタイミング関数をlinearに設定
        timingFunction: 'linear'
    });

    // リセットボタンがクリックされたときの処理を定義
    const handleReset = async (event) => {
        // デフォルトのイベントをキャンセル
        event.preventDefault();
        // ローディングアニメーションを開始
        // ローディングアニメーションの継続時間を5秒に設定
        page.start({ duration: 5000 });

        // メールアドレスが@や.を含む有効なメールアドレスかどうかをチェック
        if (!email.match(/^\S+@\S+\.\S+$/)) {
            // 通知を表示
            notice({
                title: "無効なメールアドレス",
                description: "有効なメールアドレスを入力してください。",
            });
            return;
        }

        // メール送信処理を非同期で実行
        try {
            // 2秒待機（仮）
            await new Promise(resolve => setTimeout(resolve, 2000));
            // 通知を表示
            notice({
                title: "メール送信完了",
                description: "認証コードがメールアドレスに送信されました。",
            });
            // モーダルを開く
            onOpen();
            // ローディングアニメーションを終了
        } finally {
            page.finish();
        }
    };

    // PINコード入力が完了したときの処理を定義
    const handlePinComplete = async (value) => {
        page.start({ duration: 5000 });
        try {
            // 2秒待機（仮）
            await new Promise(resolve => setTimeout(resolve, 2000));
            // PINコードの検証処理を実装
            // ここではダミーのPINコード「1234」として検証
            if (value === '1234') {
                
                page.finish();
                // 通知を表示
                notice({
                    title: "認証成功",
                    description: "パスワードリセットが完了しました。"
                });
                navigate('/');
            } else {
                notice({
                    title: "認証失敗",
                    description: "入力されたPINが正しくありません。"
                });
            }
        } catch (error) {
            notice({
                title: "エラー",
                description: "認証中にエラーが発生しました。"
            });
        } finally {
            page.finish();
        }
    };

    // モーダルを閉じる処理を定義
    const attemptCloseModal = () => {
        // モーダルを閉じる前に確認モーダルを開く
        onConfirmOpen();
    };

    // 確認モーダルを閉じる処理を定義
    // モーダルを閉じる処理を定義
    const confirmClose = () => {
        // 確認モーダルを閉じる
        onConfirmClose();
        // モーダルを閉じる
        onClose();
    };

    // レンダリング処理
    return (
        // レンダリングする要素をdiv要素で囲む
        <div className="container">
            <Box flex="1" w="full" h="100vh" animation={colorChangeAnimation} display="flex" alignItems="center" justifyContent="center">
                <p>パスワードリセット手続き</p>
            </Box>
            {/* ログインフォームを表示 */}
            <div className="right-panel" style={{ flexDirection: 'column' }}>
                <Link to="/">
                    {/* ロゴ画像を表示 */}
                    <img src={logoImage} alt="Company Logo" style={{ width: '10rem', height: 'auto', marginBottom: '20px', cursor: 'pointer' }} />
                </Link>
                {/* ページのタイトルを表示 */}
                <h1>パスワードを忘れた場合</h1>
                {/* ページの説明を表示 */}
                <p>ご登録いただいたメールアドレスにパスワード再設定のための認証コードを送ります</p>
                {/* パスワードリセットフォームを表示 */}
                <form onSubmit={handleReset} style={{ width: '100%' }}>
                    {/* メールアドレス入力欄を表示 */}
                    <FormControl label="メールアドレス">
                        {/* メールアドレス入力欄を表示 */}
                        <Input type='email' placeholder='メールアドレス' value={email} onChange={e => setEmail(e.target.value)} />
                    </FormControl>
                    {/* 認証コード送信ボタンを表示 */}
                    <div className='confirm-links'>
                        <Button type='submit'>認証コードを送信する</Button>
                    </div>
                    {/* 戻るボタンを表示 */}
                    <div style={{ marginTop: '20px' }}>
                        <Link to="/"><Button>戻る</Button></Link>
                    </div>
                </form>
            </div>

            {/* パスワードリセットモーダルを表示 */}
            <Modal isOpen={isOpen} onClose={attemptCloseModal}>
                <ModalOverlay bg="blackAlpha.300" backdropFilter="blur(10px)" />
                <ModalHeader>確認コード入力</ModalHeader>
                <ModalBody>
                    {`${email}に認証コードを送信しました。お送りしたメールに記載している確認コードをご確認の上、下記に入力してください。`}
                    <PinInput onComplete={handlePinComplete}>
                        <PinInputField />
                        <PinInputField />
                        <PinInputField />
                        <PinInputField />
                    </PinInput>
                </ModalBody>
                <ModalFooter>
                    <Button onClick={attemptCloseModal}>閉じる</Button>
                </ModalFooter>
            </Modal>

            {/* 確認モーダルを表示 */}
            <Modal isOpen={isConfirmOpen} onClose={onConfirmClose}>
                <ModalOverlay />
                <ModalHeader>確認</ModalHeader>
                <ModalBody>モーダルを閉じますか？</ModalBody>
                <ModalFooter>
                    <Button onClick={confirmClose}>閉じる</Button>
                    <Button onClick={onConfirmClose}>キャンセル</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
}

export default ResetPass;
