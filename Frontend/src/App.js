import React from 'react';
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
import SignUp from './Authentication/SignUp';
import VerifyEmail from './Authentication/VerifyEmail';

function App() {
  return (
    <BrowserRouter>
      <div>
        <h1>Welcome!</h1>
        <p>
          Don't have an account?{' '}
          <Link to="/signup">Sign up here!</Link>
        </p>
      </div>
      <Routes>
        <Route path="/signup" element={<SignUp />} />
        <Route path="/verify-email" element={<VerifyEmail />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;