import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Box,
  Button,
  Input,
  FormControl,
  Label,
  HelperMessage,
  useLoading,
  Modal,
  ModalOverlay,
  ModalCloseButton,
  ModalHeader,
  ModalBody,
  ModalFooter,
  useDisclosure,
  useAnimation
} from '@yamada-ui/react';
import AnimatePage from './AnimatePage';

function Signup() {
  const { page } = useLoading();
  const [isSubmitting, setSubmitting] = useState(false);
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [loadingMessage, setLoadingMessage] = useState("");

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
      // Implement actual signup logic here
    } finally {
      setSubmitting(false);
      page.finish();
      setLoadingMessage("読み込み中"); // Clear the loading message
    }
  };

  return (
    <AnimatePage direction="left">
      <div className="container">
        <Box flex="1" w="full" h="100vh" animation={colorChangeAnimation} display="flex" alignItems="center" justifyContent="center">
          <p>Welcome to the signup page</p>
        </Box>
        <div className="right-panel">
          <form onSubmit={handleSignup}>
            <FormControl isRequired label="ユーザー名" errorMessage={!userName && "ユーザー名は必須です。"}>
              <Label>ユーザー名</Label>
              <Input type='text' placeholder='ユーザー名' value={userName} onChange={(e) => setUserName(e.target.value)} />
              <HelperMessage>使用するユーザー名を入力してください。</HelperMessage>
            </FormControl>
            <FormControl isRequired label="パスワード" isInvalid={password.length < 6} errorMessage={(password.length < 6) && "パスワードは6文字以上です。"}>
              <Label>パスワード</Label>
              <Input type='password' placeholder='パスワード' value={password} onChange={(e) => setPassword(e.target.value)} />
            </FormControl>
            <FormControl isRequired label="パスワード確認" isInvalid={password !== passwordConfirm} errorMessage={(password !== passwordConfirm) && "パスワードが一致しません。"}>
              <Label>パスワード確認</Label>
              <Input type='password' placeholder='パスワード確認' value={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)} />
            </FormControl>
            <div className='submit-links'>
              <Button type='submit' isLoading={isSubmitting} loadingText="登録中...">登録</Button>
            </div>
            <div className="return-to-login">
              <Link to="/"><Button>ログイン画面に戻る</Button></Link>
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
        </div>
      </div>
    </AnimatePage>
  );
}

export default Signup;
