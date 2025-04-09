import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link, useNavigate} from "react-router-dom";
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
import SessionDetails from './YourSessions/SessionDetails.js';
import SpeakerSessions from "./Dropdowns/Speaker/SpeakerSessions.js";
import NotificationBell from "./NotificationBell.js";
import EventAnalytics from "./Dropdowns/Organizer/EventAnalytics.js";
import EventAnalyticsPage from "./Dropdowns/Organizer/EventAnalyticsPage.js";


const events = [
  {id: 1, image: "/images/seminar-image.jpeg", title: "Unlock new perspectives and ignite your curiosity!", button: "Find Seminars", type: "SEMINAR"},
  {id: 2, image: "/images/webinar-image.jpeg", title: "Learn from anywhere, grow from everywhere!", button: "Find Webinars", type: "WEBINAR"},
  {id: 3, image: "/images/workshop-image.jpeg", title: "Get hands-on experience with industry pros!", button: "Find Workshops", type: "WORKSHOP"},
  {id: 4, image: "/images/conference-image.jpg", title: "Get inspired by the best in the industry!", button: "Find Conferences", type: "CONFERENCE"},
];

const Home = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate();

  const handleCarouselClick = () => {
    const selectedType = events[currentIndex].type;
    navigate(`/attendees?type=${selectedType}`);
  };

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
            <button
              onClick={handleCarouselClick}
              className="mt-4 bg-[#D9C2A3] text-[#2E2E2E] px-6 py-3 rounded-md text-lg font-semibold transition duration-300 hover:bg-[#C4A88E]"
            >
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

const SuccessPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-[#E3D5C8] flex flex-col justify-center items-center">
      <h1 className="text-3xl font-bold text-[#2E2E2E] mb-6">Payment Successful!</h1>
      <button
        onClick={() => navigate("/attendees")}
        className="bg-[#D9C2A3] text-[#2E2E2E] px-6 py-2 rounded hover:bg-[#C4A88E] transition"
      >
        Back to Events
      </button>
    </div>
  );
};

const CancelPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-[#FDECEA] flex flex-col justify-center items-center">
      <h1 className="text-3xl font-bold text-red-600 mb-6">Payment Canceled</h1>
      <button
        onClick={() => navigate("/attendees")}
        className="bg-[#FBB6B6] text-[#2E2E2E] px-6 py-2 rounded hover:bg-[#f69393] transition"
      >
        Back to Events
      </button>
    </div>
  );
};

const PrivacyPolicy = () => {
  return (
    <div className="min-h-screen bg-[#E3D5C8] p-8 text-[#2E2E2E]">
      <h1 className="text-3xl font-bold mb-4">Privacy Policy</h1>
      <p className="mb-4">
        We are committed to protecting your privacy. Any information we collect is used solely to improve your experience.
      </p>
      <p className="mb-4">
        We do not share your personal data with third parties without your consent. By using our services, you agree to this policy.
      </p>
    </div>
  );
};

const TermsOfService = () => {
  return (
    <div className="min-h-screen bg-[#E3D5C8] p-8 text-[#2E2E2E]">
      <h1 className="text-3xl font-bold mb-4">Terms of Service</h1>
      <p className="mb-4">
        By accessing or using our platform, you agree to be bound by these Terms. If you do not agree, please discontinue use.
      </p>
      <p className="mb-4">
        We reserve the right to update these terms at any time. Continued use of the service constitutes acceptance of the new terms.
      </p>
    </div>
  );
};

const Contact = () => {
  return (
    <div className="min-h-screen bg-[#E3D5C8] p-8 text-[#2E2E2E]">
      <h1 className="text-3xl font-bold mb-4">Contact Us</h1>
      <p className="mb-4">Need help or have questions?</p>
      <ul className="list-disc ml-6 space-y-2">
        <li>Email: <a href="mailto:support@sees.com" className="underline">support@sees.com</a></li>
        <li>Phone: +1 (123) 456-7890</li>
      </ul>
    </div>
  );
};



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
    localStorage.removeItem("email");
    setIsLoggedIn(false);
    window.location.href = "/"; // Refresh to force re-render + redirect to homepage
  };

  return (
    <Router>
      <div className="min-h-screen]">
        {/* Navigation Bar */}
        <nav className={`fixed top-0 w-full z-50 transition-all duration-300 shadow-md backdrop-blur-md ${
          isScrolled
            ? "bg-[#E3D5C8] py-3 h-[60px] border-b border-[#C4A88E]"
            : "bg-[#E3D5C8]/80 py-4 h-[64px] border-b border-[#C4A88E]"
        }`}>
          <div className="max-w-7xl mx-auto grid grid-cols-12 items-center w-full px-6">
            
          {/* Left: S E E S + Home Icon (3/12) */}
          <div className="col-span-3 flex items-center space-x-4">
            {/* S E E S Logo */}
            <Link
              to="/"
              className="text-[#8B5E3C] font-serif font-bold text-xl tracking-wide hover:text-[#2E2E2E] transition"
            >
              S E E S
            </Link>

            {/* Separate Home Icon Link */}
            <Link
              to="/"
              className="p-1 rounded"
              title="Home"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={1.5}
                stroke="currentColor"
                className="w-6 h-6 text-[#8B5E3C] hover:text-[#2E2E2E] transition"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M3 9.75L12 4.5l9 5.25M4.5 10.5V19.5a.75.75 0 00.75.75h3.75v-5.25a.75.75 0 01.75-.75h4.5a.75.75 0 01.75.75V20.25h3.75a.75.75 0 00.75-.75V10.5"
                />
              </svg>
            </Link>
          </div>

            {/* Center: Nav Links (6/12) */}
            <div className="col-span-6 hidden md:flex justify-center items-center gap-x-10 whitespace-nowrap">
 
              <button
                onClick={() => {
                  const userId = localStorage.getItem("userId");
                  const role = localStorage.getItem("role");

                  if (!userId || role !== "ORGANIZER") {
                    const confirmLogin = window.confirm("You must be logged in as an organizer to access event planning. Log in now?");
                    if (confirmLogin) {
                      handleLogout(); // clears all session data
                      window.location.href = "/login";
                    }
                  } else {
                    window.location.href = "/event-planning";
                  }
                }}
                className="relative text-[#5A5958] text-lg font-medium tracking-widest transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]"
              >
                Plan Events
              </button>


              <button
                onClick={() => {
                  const userId = localStorage.getItem("userId");
                  const role = localStorage.getItem("role");

                  if (!userId || role !== "ATTENDEE") {
                    const confirmLogin = window.confirm("You must be logged in as an attendee to register. Log in now?");
                    if (confirmLogin) {
                      handleLogout(); // clear everything
                      window.location.href = "/login";
                    }
                  } else {
                    window.location.href = "/attendees";
                  }
                }}
                className="relative text-[#5A5958] text-lg font-medium tracking-widest transition-all duration-200 before:absolute before:bottom-0 before:left-0 before:w-0 before:h-[2px] before:bg-[#2E2E2E] before:transition-all before:duration-300 hover:before:w-full hover:text-[#2E2E2E]"
              >
                Attend Events
              </button>
            </div>


            {/* Right: Notification + Login/Menu (3/12) */}
            <div className="col-span-3 flex justify-end items-center space-x-4">
              <NotificationBell />
              {isLoggedIn ? (
                <DropdownMenu isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
              ) : (
                <Link
                  to="/login"
                  className="w-[90px] text-center bg-[#D9C2A3] text-[#2E2E2E] px-4 py-[6px] rounded-md text-lg leading-none font-semibold transition duration-300 hover:bg-[#C4A88E]"
                >
                  Log in
                </Link>
              )}
            </div>

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
            <Route path="/payments" element={<FinancialDashboard />} />
            <Route path="/promotion" element={<Promotion />} />
            <Route path="/success" element={<SuccessPage />} />
            <Route path="/cancel" element={<CancelPage />} />
            <Route path="/sessions" element={< UsersSessions />} />
            <Route path="/dropdowns/speaker/speaker-offers" element={<SpeakerOffers />} />
            <Route path="/contact-speakers/:eventId" element={<ContactSpeakers />} />
            <Route path="/reach-out-to-speakers/:sessionId" element={<SendOfferToSpeaker />} />
            <Route path="/session-details/:sessionId" element={<SessionDetails />} />
            <Route path="/chat/:chatroomId" element={<ChatRoomContainer />} />
            <Route path="/dropdowns/speaker/speaker-sessions"   element={<SpeakerSessions />}/>
            <Route path="/dropdowns/organizer/event-analytics" element={<EventAnalytics />} />          
            <Route path="/dropdowns/organizer/EventsAnalyticsDetails/:eventId" element={<EventAnalyticsPage />} /> 
            <Route path="/privacy-policy" element={<PrivacyPolicy />} />
            <Route path="/terms-of-service" element={<TermsOfService />} />
            <Route path="/contact" element={<Contact />} />
          </Routes>

          {/* Footer */}
          <footer className="bg-[#E3D5C8] border-t border-[#C4A88E] text-[#5A5958] text-center py-4 mt-8 text-sm">
            <p>© {new Date().getFullYear()} Smart Education Events System. All rights reserved.</p>
            <div className="flex justify-center space-x-4 mt-2 text-xs">
              <Link to="/privacy-policy" className="hover:underline">Privacy Policy</Link>
              <Link to="/terms-of-service" className="hover:underline">Terms</Link>
              <Link to="/contact" className="hover:underline">Contact</Link>
            </div>
          </footer>

        </div>
      </div>
    </Router>

    
  );
}

export default App;