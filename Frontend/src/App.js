import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Login from "./Authentication/Login";
import SignUp from "./Authentication/SignUp";
import ChatRoomContainer from './chat/ChatRoomContainer';

const Home = () => (
<div className="relative w-full max-w-7xl mx-auto h-[90vh] mt-20 overflow-hidden rounded-lg">
  {/* Background Image */}
  <img
    src="/images/event-image.webp"
    alt="Event"
    className="w-full h-full object-cover object-top rounded-lg"
    style={{ objectPosition: "top 20%" }} // Crops the top part
  />

  {/* Overlay (Now stays within image borders) */}
  <div className="absolute inset-0 bg-black/50 rounded-lg pointer-events-none"></div>

  {/* Centered Text Over Image */}
  <div className="absolute inset-0 flex flex-col items-center justify-center text-center px-6">
    <h1 className="text-4xl font-extrabold text-white">
      Welcome to Smart Education Events System
    </h1>
    <p className="text-lg text-gray-200 max-w-2xl">
      Register, plan, and engage with educational events effortlessly.
    </p>
    <div className="mt-6 flex space-x-4">
      <Link to="/signup" className="bg-[#D9C2A3] text-[#2E2E2E] px-6 py-3 rounded-md text-lg font-semibold transition duration-300 hover:bg-[#C4A88E]">
        Get Started
      </Link>

    </div>
  </div>
</div>
);

const EventPlanning = () => <h2 className="text-2xl font-bold text-center mt-6">Event Planning Page</h2>;
const Attendees = () => <h2 className="text-2xl font-bold text-center mt-6">Attendees Management Page</h2>;
const Networking = () => <h2 className="text-2xl font-bold text-center mt-6">Networking & Engagement Page</h2>;
const Payments = () => <h2 className="text-2xl font-bold text-center mt-6">Payment & Financial Management Page</h2>;

function App() {
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 50);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <Router>
      <div className="min-h-screen]">
        {/* Navigation Bar */}
        <nav className={`fixed top-0 w-full z-50 transition-all duration-300 shadow-md backdrop-blur-md ${isScrolled ? "bg-[#E3D5C8]/80 py-2 h-12 border-b border-[#C4A88E]" : "bg-[#E3D5C8]/80 py-4 h-16 border-b border-[#C4A88E]"}`}>
          <div className="max-w-6xl mx-auto flex items-center justify-between w-full px-6">
            {/* Logo */}
            <Link to="/" className="relative text-[#2E2E2E] font-medium text-lg">
              Smart Events
            </Link>

            {/* Navigation Links */}
            <div className="hidden md:flex space-x-6">
              <Link to="/" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Home
              </Link>
              <Link to="/event-planning" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Event Planning
              </Link>
              <Link to="/attendees" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Attendees
              </Link>
              <Link to="/networking" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Networking
              </Link>
              <Link to="/payments" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Payments
              </Link>
              <Link to="/chat" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Chat Room
              </Link>
            </div>

            {/* Login Button */}
            <Link to="/login" className="relative text-[#2E2E2E] text-lg font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
              Login
            </Link>
          </div>
        </nav>
        {/* Adjust spacing so content does not go under the fixed navbar */}
        <div className="mt-16">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/event-planning" element={<EventPlanning />} />
            <Route path="/attendees" element={<Attendees />} />
            <Route path="/networking" element={<Networking />} />
            <Route path="/payments" element={<Payments />} />
            <Route path="/chat" element={<ChatRoomContainer />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
