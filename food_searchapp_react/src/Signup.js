import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Input,
  FormControl,
  Label,
  Image,
  HelperMessage,
  useLoading,
  Modal,
  ModalOverlay,
  ModalCloseButton,
  ModalHeader,
  ModalBody,
  ModalFooter,
  useDisclosure,
  useAnimation,
  PinInput,
  PinInputField,
  useNotice
} from '@yamada-ui/react';
import AnimatePage from './AnimatePage';

const logoImage = process.env.PUBLIC_URL + '/images/FOOD_search.jpg';

function Signup() {
  const navigate = useNavigate();
  const notice = useNotice({
    component: ({ description }) => (
      <Box color="white" py={3} px={4} bg="purple.500">
        {description}
      </Box>
    ),
    style: { maxW: "100%", minW: "100%" }
  });
  const { page } = useLoading();
  const [isSubmitting, setSubmitting] = useState(false);
  const [userName, setUserName] = useState('');
  const [userNameTouched, setUserNameTouched] = useState(false);
  const [password, setPassword] = useState('');
  const [passwordTouched, setPasswordTouched] = useState(false);
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [passwordConfirmTouched, setPasswordConfirmTouched] = useState(false);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [loadingMessage, setLoadingMessage] = useState("");
  const [isPinModalOpen, setPinModalOpen] = useState(false);
  const [pin, setPin] = useState('');



  const colorChangeAnimation = useAnimation({
    keyframes: {
      '0%': { bg: 'red.500' },
      '20%': { bg: 'green.500' },
      '40%': { bg: 'purple.500' },
      '60%': { bg: 'yellow.500' },
      '80%': { bg: 'blue.500' },
      '100%': { bg: 'red.500' },
    },
    duration: '10s',
    iterationCount: 'infinite',
    timingFunction: 'linear'
  });

  const handlePinComplete = async (value) => {
    console.log("入力されたPIN: ", value);
    page.start();
    try {
      // ここで非同期のPIN検証ロジックを実装する
      await new Promise(resolve => setTimeout(resolve, 2000)); // ダミーの非同期処理の実装
      if (value === '1234') {
        console.log("PINが正しいです。");
        notice({ description: "登録が完了しました。ログインをお願いします。" }); // 通知を表示
        setPinModalOpen(false);
        navigate('/'); // Signinページに遷移
      } else {
        console.log("PINが間違っています。");
        // エラーメッセージを表示するなどの処理をここに追加
      }
    } catch (error) {
      console.error("PIN検証中にエラーが発生しました", error);
    } finally {
      page.finish();
    }
  };




  const handleSignup = async (event) => {
    event.preventDefault();
    if (!userName || !password || password.length < 6 || password !== passwordConfirm) {
      onOpen();
      return;
    }
    setSubmitting(true);
    page.start();
    setLoadingMessage("読み込み中...");

    try {
      await new Promise(resolve => setTimeout(resolve, 2000));

      //登録のロジックを実装
      setLoadingMessage("");
      setPinModalOpen(true); // 登録成功時にPIN入力モーダルを開く
    } finally {
      setSubmitting(false);
      page.finish();
      setLoadingMessage(""); // ローディングメッセージをクリア
    }
  };

  return (
    <AnimatePage direction="left">
      <div className="container">
        <Box flex="1" w="full" h="100vh" animation={colorChangeAnimation} display="flex" alignItems="center" justifyContent="center">
          <p>Welcome to the signup page</p>
        </Box>
        <div className="right-panel">
          <Link to="/">
            <Image src={logoImage} alt="Company Logo" marginBottom="20px" style={{ width: '10rem', height: 'auto', cursor: 'pointer' }} />
          </Link>
          <form onSubmit={handleSignup}>
            <FormControl isRequired label="ユーザー名" errorMessage={userNameTouched && !userName && "ユーザー名は必須です。"}>
              <Label>ユーザー名</Label>
              <Input type='text' placeholder='ユーザー名' value={userName} onChange={(e) => { setUserName(e.target.value); setUserNameTouched(true); }} />
              <HelperMessage>使用するユーザー名を入力してください。</HelperMessage>
            </FormControl>
            <FormControl isRequired label="メールアドレス">
              <Label>メールアドレス</Label>
              <Input type='email' placeholder='メールアドレス' />
            </FormControl>
            <FormControl isRequired label="パスワード" isInvalid={passwordTouched && password.length < 6} errorMessage={passwordTouched && (password.length < 6) && "パスワードは6文字以上です。"}>
              <Label>パスワード</Label>
              <Input type='password' placeholder='パスワード' value={password} onChange={(e) => { setPassword(e.target.value); setPasswordTouched(true); }} />
            </FormControl>
            <FormControl isRequired label="パスワード確認" isInvalid={passwordConfirmTouched && password !== passwordConfirm} errorMessage={passwordConfirmTouched && (password !== passwordConfirm) && "パスワードが一致しません。"}>
              <Label>パスワード確認</Label>
              <Input type='password' placeholder='パスワード確認' value={passwordConfirm} onChange={(e) => { setPasswordConfirm(e.target.value); setPasswordConfirmTouched(true); }} />
            </FormControl>
            <div className='submit-links'>
              <Button type='submit' isLoading={isSubmitting} loadingText="登録中...">登録</Button>
            </div>
            <div className="return-to-login">
              <Link to={{ pathname: "/", state: { fromSignup: true } }}>
                <Button>ログイン画面に戻る</Button>
              </Link>
            </div>
          </form>
          {loadingMessage && (
            <Modal isOpen={true}>
              <ModalOverlay />
              <ModalHeader>処理中</ModalHeader>
              <ModalBody>{loadingMessage}</ModalBody>
              <ModalFooter>
                <Button onClick={() => setLoadingMessage("")}>閉じる</Button>
              </ModalFooter>
            </Modal>
          )}
          <Modal isOpen={isOpen} onClose={onClose}>
            <ModalOverlay />
            <ModalCloseButton />
            <ModalHeader>入力エラー</ModalHeader>
            <ModalBody>すべての必須項目を適切に入力してください。パスワードは6文字以上で、一致する必要があります。</ModalBody>
            <ModalFooter>
              <Button onClick={onClose}>閉じる</Button>
            </ModalFooter>
          </Modal>
          <Modal isOpen={isPinModalOpen} onClose={() => setPinModalOpen(false)}>
            <ModalHeader>PINコード確認</ModalHeader>
            <ModalBody>
              <PinInput value={pin} onChange={setPin} onComplete={handlePinComplete}>
                <PinInputField />
                <PinInputField />
                <PinInputField />
                <PinInputField />
              </PinInput>
            </ModalBody>
            <ModalFooter>
              <Button onClick={() => setPinModalOpen(false)}>閉じる</Button>
            </ModalFooter>
          </Modal>

        </div>
      </div>
    </AnimatePage>
  );
}

export default Signup;
