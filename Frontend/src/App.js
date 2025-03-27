import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from "react-router-dom";
import Login from "./Authentication/Login";
import SignUp from "./Authentication/SignUp";
import VerifyEmail from "./Authentication/VerifyEmail";
import ChatRoomContainer from './chat/ChatRoomContainer';
import EventPlanning from './eventplanning/EventPlanning';
import FinancialDashboard from "./Payment & Financial Management/FinancialDashboard";
import AttendeeManagement from "./Attendee/AttendeeManagement";
import Promotion from "./promotion/Promotion";
import UsersSessions from "./YourSessions/UsersSessions.js"
import DropdownMenu from "./Dropdowns/DropdownMenu.js";
import SpeakerOffers from "./Dropdowns/Speaker/SpeakerOffers.js";
import ContactSpeakers from "./eventplanning/ContactSpeakers.js"
import SendOfferToSpeaker from "./eventplanning/SendOfferToSpeaker.js"





const Home = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const events = [
    { id: 1, image: "/images/seminar-image.jpeg", title: "Unlock new perspectives and ignite your curiosity!", button: "Find Seminars" },
    { id: 2, image: "/images/webinar-image.jpeg", title: "Learn from anywhere, grow from everywhere!", button: "Find Webinars" },
    { id: 3, image: "/images/workshop-image.jpeg", title: "Get hands-on experience with industry pros!", button: "Find Workshops" },
    { id: 4, image: "/images/conference-image.jpg", title: "Get inspired by the best in the industry!", button: "Find Conferences" },
  ];

  

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % events.length);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  const prevSlide = () => setCurrentIndex((prevIndex) => (prevIndex - 1 + events.length) % events.length);
  const nextSlide = () => setCurrentIndex((prevIndex) => (prevIndex + 1) % events.length);

  return (
    <div className="flex flex-col items-center w-full">
      <div className="relative w-full max-w-7xl mx-auto h-[50vh] mt-8 overflow-hidden rounded-lg">
        <img src="/images/event-image.webp" alt="Event" className="w-full h-full object-cover rounded-lg" style={{ objectPosition: "center 60%" }} />
        <div className="absolute inset-0 bg-black/50 rounded-lg pointer-events-none"></div>
        <div className="absolute inset-0 flex flex-col items-center justify-center text-center px-6">
          <h1 className="text-4xl font-extrabold text-white">Welcome to Smart Education Events System</h1>
          <p className="text-lg text-gray-200 max-w-2xl">Register, plan, and engage with educational events effortlessly.</p>
          <div className="mt-6 flex space-x-4">
            <Link to="/signup" className="bg-[#D9C2A3] text-[#2E2E2E] px-6 py-3 rounded-md text-lg font-semibold transition duration-300 hover:bg-[#C4A88E]">Get Started</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

const Networking = () => <h2 className="text-2xl font-bold text-center mt-6">Networking & Engagement Page</h2>;
const Unauthorized = () => <h2 className="text-center mt-10 text-xl text-red-600">Access Denied: Organizers Only</h2>;

function App() {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const [userRole, setUserRole] = useState(() => localStorage.getItem("role") || "");

  useEffect(() => {
    const handleScroll = () => setIsScrolled(window.scrollY > 50);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    const handleStorageChange = () => {
      setIsLoggedIn(!!localStorage.getItem("token"));
      setUserRole(localStorage.getItem("role") || "");
    };
    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    setIsLoggedIn(false);
    setUserRole("");
    window.location.href = "/";
  };

  return (
    <Router>
      <div className="min-h-screen]">
        <nav className={`fixed top-0 w-full z-50 transition-all duration-300 shadow-md backdrop-blur-md ${isScrolled ? "bg-[#E3D5C8] py-2 h-12 border-b border-[#C4A88E]" : "bg-[#E3D5C8]/80 py-4 h-16 border-b border-[#C4A88E]"}`}>
          <div className="max-w-6xl mx-auto flex items-center justify-between w-full px-6">
            <Link to="/" className="relative text-[#8B5E3C] font-serif font-bold text-xl tracking-wide drop-shadow-lg">S E E S</Link>
            <div className="hidden md:flex space-x-6">
              <Link to="/" className="relative text-[#8B5E3C] font-serif font-bold text-xl tracking-wide drop-shadow-lg">Home</Link>
              <Link to="/event-planning" className="relative text-[#5A5958]">Event Planning</Link>
              <Link to="/attendees" className="relative text-[#5A5958]">Attendees</Link>
              <Link to="/networking" className="relative text-[#5A5958]">Networking</Link>
              {userRole === "ORGANIZER" && (
                <Link to="/payments" className="relative text-[#5A5958]">Financial Management</Link>
              )}
              <Link to="/chat" className="relative text-[#5A5958]">Chat Room</Link>
              <Link to="/promotion" className="relative text-[#5A5958]">Promotions</Link>
              <Link to="/sessions" className="relative text-[#5A5958]">Your Sessions</Link>
            </div>
            {isLoggedIn ? (
              <button onClick={handleLogout} className="relative text-[#2E2E2E] text-lg">Log Out</button>
            ) : (
              <Link to="/login" className="relative text-[#2E2E2E] text-lg">Log In</Link>
            )}
          </div>
        </nav>

        <div className="mt-16">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/verify-email" element={<VerifyEmail />} />
            <Route path="/event-planning" element={<EventPlanning />} />
            <Route path="/attendees" element={<AttendeeManagement />} />
            <Route path="/networking" element={<Networking />} />
            <Route path="/payments" element={userRole === "ORGANIZER" ? <FinancialDashboard /> : <Unauthorized />} />
            <Route path="/chat" element={<ChatRoomContainer />} />
            <Route path="/promotion" element={<Promotion />} />
            <Route path="/sessions" element={<UsersSessions />} />
            <Route path="/dropdowns/speaker/speaker-offers" element={<SpeakerOffers />} />
            <Route path="/contact-speakers/:eventId" element={<ContactSpeakers />} />
            <Route path="/reach-out-to-speakers/:sessionId" element={<SendOfferToSpeaker />} />
          </Routes>

          <footer className="bg-[#E3D5C8] border-t border-[#C4A88E] text-[#5A5958] text-center py-4 mt-8 text-sm">
            © {new Date().getFullYear()} Smart Education Events System. All rights reserved.
          </footer>
        </div>
      </div>
    </Router>




  );



}



export default App;

