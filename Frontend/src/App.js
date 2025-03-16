import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import "./App.css";
import Login from "./Authentication/Login";
import SignUp from "./Authentication/SignUp";
import ChatRoomContainer from './chat/ChatRoomContainer';

const Home = () => <h2>Welcome to Smart Education Events System</h2>;
const EventPlanning = () => <h2>Event Planning Page</h2>;
const Attendees = () => <h2>Attendees Management Page</h2>;
const Networking = () => <h2>Networking & Engagement Page</h2>;
const Payments = () => <h2>Payment & Financial Management Page</h2>;

function App() {
  return (
    <Router>
      <div>
        <nav>
          <div className="nav-links">
            <Link to="/">Home</Link>
            <Link to="/event-planning">Event Planning</Link>
            <Link to="/attendees">Attendees</Link>
            <Link to="/networking">Networking</Link>
            <Link to="/payments">Payments</Link>
            <Link to="/chat">Chat Room</Link>
          </div>
          <Link to="/login" className="login-button">Login</Link>
        </nav>
        <hr />
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
    </Router>
  );
}

export default App;