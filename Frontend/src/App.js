import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Login from "./Authentication/Login";
import SignUp from "./Authentication/SignUp";
import VerifyEmail from "./Authentication/VerifyEmail";
import ChatRoomContainer from './chat/ChatRoomContainer';
import EventPlanning from './eventplanning/EventPlanning';
import FinancialDashboard from "./Payment & Financial Management/FinancialDashboard";
import AttendeeManagement from "./Attendee/AttendeeManagement";
import Promotion from "./promotion/Promotion";


const events = [
  {id: 1, image: "/images/seminar-image.jpeg", title: "Unlock new perspectives and ignite your curiosity!", button: "Find Seminars"},
  {id: 2, image: "/images/webinar-image.jpeg", title: "Learn from anywhere, grow from everywhere!", button: "Find Webinars"},
  {id: 3, image: "/images/workshop-image.jpeg", title: "Get hands-on experience with industry pros!", button: "Find Workshops"},
  {id: 4, image: "/images/conference-image.jpg", title: "Get inspired by the best in the industry!", button: "Find Conferences"},
];

const Home = () => {
  const [currentIndex, setCurrentIndex] = useState(0);

  // Auto-slide every 5 seconds
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % events.length);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  // Manual slide controls
  const prevSlide = () => setCurrentIndex((prevIndex) => (prevIndex - 1 + events.length) % events.length);
  const nextSlide = () => setCurrentIndex((prevIndex) => (prevIndex + 1) % events.length);

  return (
    <div className="flex flex-col items-center w-full">
      {/* Welcome Section */}
      <div className="relative w-full max-w-7xl mx-auto h-[50vh] mt-8 overflow-hidden rounded-lg">
        {/* Background Image */}
        <img
          src="/images/event-image.webp"
          alt="Event"
          className="w-full h-full object-cover rounded-lg"
          style={{ objectPosition: "center 60%" }} // Crops the top part
        />

        {/* Overlay */}
        <div className="absolute inset-0 bg-black/50 rounded-lg pointer-events-none"></div>

        {/* Centered Text */}
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

      

        {/*  Browse Events Section */}
      <div className="w-full max-w-7xl mx-auto mt-8 text-center">

      {/* Decorative Line */}
      <hr className="w-full max-w-10xl border-t border-gray-300" />
        
        <h2 className="text-3xl font-bold text-[#2E2E2E] mt-4">Browse Events</h2>
        <p className="text-lg text-[#5A5958] mt-2">
          Discover upcoming seminars, webinars, workshops, and conferences tailored for you.
        </p>        
      </div>

      {/* Carousel Section */}
      <div className="relative w-full max-w-7xl mx-auto h-[30vh] mt-10 overflow-hidden rounded-lg">
        <div className="relative w-full h-full">
          <img 
            src={events[currentIndex].image} 
            alt={events[currentIndex].title} 
            className="w-full h-full object-cover rounded-lg transition-opacity duration-500 ease-in-out"
          />

          {/* Overlay */}
          <div className="absolute inset-0 bg-black/40 rounded-lg pointer-events-none"></div>

          {/* Centered Text */}
          <div className="absolute inset-0 flex flex-col items-center justify-center text-center px-6">
            <h1 className="text-3xl font-extrabold text-white">{events[currentIndex].title}</h1>
            <button className="mt-4 bg-[#D9C2A3] text-[#2E2E2E] px-6 py-3 rounded-md text-lg font-semibold transition duration-300 hover:bg-[#C4A88E]">
              {events[currentIndex].button}
            </button>
          </div>
        </div>

        {/* Navigation Arrows */}
        <button 
          onClick={prevSlide} 
          className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-black/30 text-white p-2 rounded-full hover:bg-black/50"
        >
          ◀
        </button>
        <button 
          onClick={nextSlide} 
          className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-black/30 text-white p-2 rounded-full hover:bg-black/50"
        >
          ▶
        </button>

        {/* Dots Indicator */}
        <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
          {events.map((_, index) => (
            <div 
              key={index} 
              className={`w-3 h-3 rounded-full transition-all duration-300 ${index === currentIndex ? "bg-[#D9C2A3] scale-125" : "bg-gray-400"}`}
              onClick={() => setCurrentIndex(index)}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

const Networking = () => <h2 className="text-2xl font-bold text-center mt-6">Networking & Engagement Page</h2>;
const Payments = () => <h2 className="text-2xl font-bold text-center mt-6">Payment & Financial Management Page</h2>;

function App() {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token")); // check if token exists

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 50);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    const handleStorageChange = () => {
      setIsLoggedIn(!!localStorage.getItem("token"));
    };
    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);
  
  // Logout function
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("tokenExpiration");
    localStorage.removeItem("userId");
    localStorage.removeItem("role");
    setIsLoggedIn(false);
    window.location.href = "/"; // Refresh to force re-render + redirect to homepage
  };

  return (
    <Router>
      <div className="min-h-screen]">
        {/* Navigation Bar */}
        <nav className={`fixed top-0 w-full z-50 transition-all duration-300 shadow-md backdrop-blur-md ${isScrolled ? "bg-[#E3D5C8] py-2 h-12 border-b border-[#C4A88E]" : "bg-[#E3D5C8]/80 py-4 h-16 border-b border-[#C4A88E]"}`}>
          <div className="max-w-6xl mx-auto flex items-center justify-between w-full px-6">
            {/* Logo */}
            <Link to="/" className="relative text-[#8B5E3C] font-serif font-bold text-xl tracking-wide drop-shadow-lg">
              S E E S
            </Link>

            {/* Navigation Links */}
            <div className="hidden md:flex space-x-6">
              <Link to="/" className="relative text-[#8B5E3C] font-serif font-bold text-xl tracking-wide drop-shadow-lg transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
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
              <Link to="/promotion" className="relative text-[#5A5958] text-medium font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]">
                Promotions
              </Link>

            </div>

            {/* Login Button */}
            {isLoggedIn ? (
              <button
                onClick={handleLogout}
                className="relative text-[#2E2E2E] text-lg font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]"
              >
                Log Out
              </button>
            ) : (
              <Link
                to="/login"
                className="relative text-[#2E2E2E] text-lg font-small transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]"
              >
                Log In
              </Link>
            )}

          </div>
        </nav>
        {/* Adjust spacing so content does not go under the fixed navbar */}
        <div className="mt-16">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/verify-email" element={<VerifyEmail />} />
            <Route path="/event-planning" element={<EventPlanning />} />
            <Route path="/attendees" element={<AttendeeManagement />} />
            <Route path="/networking" element={<Networking />} />
            <Route path="/payments" element={<FinancialDashboard />} />
            <Route path="/chat" element={<ChatRoomContainer />} />
            <Route path="/promotion" element={<Promotion />} />
          </Routes>

          {/* Footer */}
          <footer className="bg-[#E3D5C8] border-t border-[#C4A88E] text-[#5A5958] text-center py-4 mt-8 text-sm">
            © {new Date().getFullYear()} Smart Education Events System. All rights reserved.
          </footer>
        </div>
      </div>
    </Router>

    
  );
}

export default App;
