import React from "react";
import {
  Box,
  Button,
  Input,
  FormControl,
  useLoading,
  useAnimation,
} from "@yamada-ui/react";
import { Link } from "react-router-dom";

function ResetPass() {
  const { screen } = useLoading();

  const colorChangeAnimation = useAnimation({
    keyframes: {
      "0%": { bg: "blue.500" },
      "25%": { bg: "green.500" },
      "50%": { bg: "yellow.500" },
      "75%": { bg: "red.500" },
      "100%": { bg: "blue.500" },
    },
    duration: "10s",
    iterationCount: "infinite",
    timingFunction: "linear",
  });

  const handleReset = async (event) => {
    event.preventDefault();
    screen.start();

    try {
      await new Promise((resolve) => setTimeout(resolve, 2000)); // 遅延シミュレーション
      // ここにパスワードリセットロジックを追加
    } finally {
      screen.finish();
    }
  };

  return (
    <div className="container">
      <Box
        flex="1"
        w="full"
        h="100vh"
        animation={colorChangeAnimation}
        display="flex"
        alignItems="center"
        justifyContent="center"
      >
        <p>パスワードリセット手続き</p>
      </Box>
      <div className="right-panel" style={{ flexDirection: "column" }}>
        <h1>パスワードを忘れた場合</h1>
        <p>
          ご登録いただいたメールアドレスにパスワード再設定のための認証コードを送ります
        </p>
        <form onSubmit={handleReset} style={{ width: "100%" }}>
          <FormControl label="メールアドレス">
            <Input type="email" placeholder="メールアドレス" />
          </FormControl>
          <Button type="submit">認証コードを送信する</Button>
          <div style={{ marginTop: "20px" }}>
            <Link to="/">
              <Button>戻る</Button>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ResetPass;
