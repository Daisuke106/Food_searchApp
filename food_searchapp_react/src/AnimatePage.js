// AnimatePage.js
import { motion } from 'framer-motion';

const pageVariants = (direction = 'right') => ({
    initial: {
        opacity: 0,
        x: direction === 'right' ? '100vw' : '-100vw',
    },
    in: {
        opacity: 1,
        x: 0
    },
    out: {
        opacity: 0,
        x: direction === 'right' ? '-100vw' : '100vw',
    }
});

const pageTransition = {
    type: "tween",
    ease: "anticipate",
    duration: 0.5
};

const AnimatePage = ({ children, direction = 'right' }) => (
    <motion.div
        initial="initial"
        animate="in"
        exit="out"
        variants={pageVariants(direction)}
        transition={pageTransition}
    >
        {children}
    </motion.div>
);

export default AnimatePage;
