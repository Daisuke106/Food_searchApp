import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';
import { UIProvider } from '@yamada-ui/react';
import './App.css';
import Signin from './Signin';
import Signup from './Signup';
import ResetPass from './ResetPass';
import AnimatePage from './AnimatePage';
import Main from './Main';
import Main from './Main';

function AnimatedRoutes() {
  const location = useLocation();

  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        <Route path="/" element={<AnimatePage><Signin /></AnimatePage>} />
        <Route path="/signup" element={<AnimatePage><Signup /></AnimatePage>} />
        <Route path="/resetpass" element={<AnimatePage><ResetPass /></AnimatePage>} />
        <Route path='/main' element={<AnimatePage><Main /></AnimatePage>} />
      </Routes>
    </AnimatePresence>
  );
}

function App() {
  return (
    <UIProvider>
      <Router>
        <AnimatedRoutes />
      </Router>
    </UIProvider>
  );
}

export default App;
