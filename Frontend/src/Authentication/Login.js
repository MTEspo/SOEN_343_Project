import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/api/auth/login", {
        email,
        password,
      });

      console.log("Full login response:", response);
  
      const jwtToken = response.data.token;
      const expiration = response.data.expiresIn;
  
      console.log("JWT Tokennn:", jwtToken);
      console.log("Expires attt:", expiration);
  
      localStorage.setItem("token", jwtToken);
      localStorage.setItem("tokenExpiration", expiration);
  
      const decoded = jwtDecode(jwtToken);

      localStorage.setItem("userId", decoded.userId);
      localStorage.setItem("role", decoded.role);
      localStorage.setItem("email", email);
      localStorage.setItem("username", decoded.username);


      window.dispatchEvent(new Event("storage"));

      console.log("user id:", decoded.userId);
      console.log("role:", decoded.role);
      console.log("email:", email)
  
      alert("Logged in successfully! Happy learning!");
      navigate("/");
    } catch (err) {
      console.error("Login error:", err);
      if (err.response && err.response.status === 401) {
        setError("Invalid username or password.");
      } else {
        setError("Login failed. Please try again later.");
      }
    }
  };
  

  return (
    <div className="flex justify-center items-center min-h-screen bg-[#E3D5C8]">
      <div className="bg-white shadow-lg rounded-lg p-8 w-96">
        <h2 className="text-2xl font-bold text-[#2E2E2E] text-center mb-4">Login</h2>
        <form className="space-y-4" onSubmit={handleLogin}>
          <div>
            <label className="block text-[#5A5958] font-medium">Email:</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter email"
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <div>
            <label className="block text-[#5A5958] font-medium">Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-[#D9C2A3] text-[#2E2E2E] py-2 rounded-md font-semibold transition duration-300 hover:bg-[#C4A88E]"
          >
            Login
          </button>
          {error && <p className="text-red-500 text-sm text-center mt-2">{error}</p>}
        </form>
        <p className="text-center text-[#5A5958] mt-4">
          If you don't have an account,
          <Link to="/signup" className="text-[#2E2E2E] font-semibold hover:underline"> sign up here</Link>.
        </p>
      </div>
    </div>
  );
}

export default Login;
