import React from 'react';
import { motion } from 'framer-motion';

const imageVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1 }
};

const transition = {
    duration: 2
};

// key プロパティを imageKey にリネームして使用
const ImageFade = ({ src, imageKey }) => (
    <motion.img
        key={imageKey}  // key はここで使用する
        src={src}
        initial="hidden"
        animate="visible"
        exit="hidden"
        variants={imageVariants}
        transition={transition}
        style={{ width: '100%', height: 'auto' }}  // 画像のサイズ調整が必要であればここを変更
    />
);

export default ImageFade;