import React from 'react';
import { Link } from 'react-router-dom';
import {
    Box,
    Button,
    Input,
    FormControl,
    useLoading,
    useAnimation,
    useNotice,
} from '@yamada-ui/react';

const logoImage = process.env.PUBLIC_URL + '/images/FOOD_search.jpg';

function ResetPass() {
    const { page } = useLoading();
    const notice = useNotice({
        component: ({ description }) => (
            <Box color="white" py={3} px={4} bg="purple.500">
                {description}
            </Box>
        ),
        style: { maxW: "100%", minW: "100%" },
        duration: 10000  // 10秒間表示する
    });

    const colorChangeAnimation = useAnimation({
        keyframes: {
            '0%': { bg: 'blue.500' },
            '25%': { bg: 'green.500' },
            '50%': { bg: 'yellow.500' },
            '75%': { bg: 'red.500' },
            '100%': { bg: 'blue.500' },
        },
        duration: '10s',
        iterationCount: 'infinite',
        timingFunction: 'linear'
    });

    const handleReset = async (event) => {
        event.preventDefault();
        page.start({ duration: 5000 });  // 5秒間のローディング

        try {
            await new Promise(resolve => setTimeout(resolve, 2000)); // 2秒後に解決するプロミスを模擬
            notice({
                title: "メール送信完了",
                description: "認証コードがメールアドレスに送信されました。",
            });
        } finally {
            page.finish();
        }
    };

    return (
        <div className="container">
            <Box flex="1" w="full" h="100vh" animation={colorChangeAnimation} display="flex" alignItems="center" justifyContent="center">
                <p>パスワードリセット手続き</p>
            </Box>
            <div className="right-panel" style={{ flexDirection: 'column' }}>
                <Link to="/">
                    <img src={logoImage} alt="Company Logo" style={{ width: '10rem', height: 'auto', marginBottom: '20px', cursor: 'pointer' }} />
                </Link>
                <h1>パスワードを忘れた場合</h1>
                <p>ご登録いただいたメールアドレスにパスワード再設定のための認証コードを送ります</p>
                <form onSubmit={handleReset} style={{ width: '100%' }}>
                    <FormControl label="メールアドレス">
                        <Input type='email' placeholder='メールアドレス' />
                    </FormControl>
                    <div className='confirm-links'>
                    <Button type='submit'>認証コードを送信する</Button>
                    </div>
                    <div style={{ marginTop: '20px' }}>
                        <Link to="/"><Button>戻る</Button></Link>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default ResetPass;