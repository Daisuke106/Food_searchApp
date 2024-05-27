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
  const [email, setEmail] = useState('');
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
      const response = await fetch('http://localhost:8080/api/auth/verify-pin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, pin: value })
      });
      const data = await response.json();
      if (data.success) {
        console.log("PINが正しいです。");
        notice({ description: "登録が完了しました。ログインをお願いします。" });
        setPinModalOpen(false);
        navigate('/'); // Signinページに遷移
      } else {
        console.log("PINが間違っています。");
        notice({ description: "PINが間違っています。" });
      }
    } catch (error) {
      console.error("PIN検証中にエラーが発生しました", error);
    } finally {
      page.finish();
    }
  };

  const handleSignup = async (event) => {
    event.preventDefault();
    if (!userName || !email || !password || password.length < 6 || password !== passwordConfirm) {
      onOpen();
      return;
    }
    setSubmitting(true);
    page.start();
    setLoadingMessage("読み込み中...");

    try {
      const response = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: userName, email: email, password: password })
      });
      const data = await response.json();
      if (data.success) {
        setLoadingMessage("");
        setPinModalOpen(true); // 登録成功時にPIN入力モーダルを開く
      } else {
        notice({ description: data.message });
      }
    } catch (error) {
      console.error("ユーザー登録中にエラーが発生しました", error);
    } finally {
      setSubmitting(false);
      page.finish();
      setLoadingMessage("");
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
              <Input type='email' placeholder='メールアドレス' value={email} onChange={(e) => { setEmail(e.target.value); }} />
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
