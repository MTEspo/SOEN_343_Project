import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom';

const VerifyEmail = () => {
  const [verificationCode, setVerificationCode] = useState('');
  const [error, setError] = useState(null);
  const location = useLocation();
  const email = location.state?.email;

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/verify', {
        email,
        verificationCode
      });
      console.log(response);
      // Redirect to login page or display success message
    } catch (error) {
      if (error.response) {
        console.error('Error response:', error.response);
        setError(`Error ${error.response.status}: ${error.response.statusText}`);
      } else if (error.request) {
        console.error('Error request:', error.request);
        setError('Error: Network error');
      } else {
        console.error('Error:', error.message);
        setError(error.message);
      }
    }
  };

  return (
    <div>
      <h1>Verify Email</h1>
      {email && (
        <p>Please enter the verification code sent to your email: {email}</p>
      )}
      <form onSubmit={handleSubmit}>
        <label>
          Verification Code:
          <input
            type="text"
            value={verificationCode}
            onChange={(event) => setVerificationCode(event.target.value)}
          />
        </label>
        <br />
        <button type="submit">Verify</button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
};

export default VerifyEmail;