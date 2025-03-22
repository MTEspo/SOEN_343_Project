import React from "react";
import { Link } from "react-router-dom";

function Login() {
  return (
    <div className="flex justify-center items-center min-h-screen bg-[#E3D5C8]">
      <div className="bg-white shadow-lg rounded-lg p-8 w-96">
        <h2 className="text-2xl font-bold text-[#2E2E2E] text-center mb-4">Login</h2>
        <form className="space-y-4">
          <div>
            <label className="block text-[#5A5958] font-medium">Username:</label>
            <input 
              type="text" 
              placeholder="Enter username" 
              className="w-full px-3 py-2 border border-[#C4A88E] rounded-md focus:outline-none focus:ring-2 focus:ring-[#C4A88E]"
            />
          </div>
          <div>
            <label className="block text-[#5A5958] font-medium">Password:</label>
            <input 
              type="password" 
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