import React, { useState } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';

const VerifyEmail = () => {
  const [verificationCode, setVerificationCode] = useState('');
  const [error, setError] = useState(null);
  const [resendMessage, setResendMessage] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/verify', {
        email,
        verificationCode
      });
      console.log(response);
      alert("Email verified successfully! You can now log in.");
      navigate('/login');
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

  const handleResend = async () => {
    try {
      const res = await axios.post("http://localhost:8080/api/auth/resend", email, {
        headers: {
          "Content-Type": "text/plain"
        }
      });
      setResendMessage("Verification code sent successfully!");
      setError(null);
    } catch (err) {
      console.error("Resend failed:", err);
      setResendMessage(null);
      setError("Failed to resend verification code.");
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-[#E3D5C8]">
      <div className="bg-white shadow-lg rounded-lg p-8 w-96">
        <h2 className="text-2xl font-bold text-[#2E2E2E] text-center mb-4">Verify Email</h2>
        {email && (
          <p className="text-sm text-center text-[#5A5958] mb-4">
            Please enter the verification code sent to <strong>{email}</strong>
          </p>
        )}
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-[#5A5958] font-medium">Verification Code:</label>
            <input
              type="text"
              value={verificationCode}
              onChange={(event) => setVerificationCode(event.target.value)}
              placeholder="Enter verification code"
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-[#D9C2A3] text-[#2E2E2E] py-2 rounded-md font-semibold transition duration-300 hover:bg-[#C4A88E]"
          >
            Verify
          </button>
        </form>

        {/* Resend Button */}
        <div className="text-center mt-4">
          <button
            onClick={handleResend}
            className="text-sm text-[#2E2E2E] hover:underline"
          >
            Resend Verification Code
          </button>
          {resendMessage && (
            <p className="text-green-600 text-sm mt-2">{resendMessage}</p>
          )}
          {error && (
            <p className="text-red-500 text-sm mt-2">{error}</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default VerifyEmail;
