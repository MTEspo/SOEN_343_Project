import React, { useEffect, useState } from "react";
import axios from "axios";
import { useSearchParams } from "react-router-dom";


const API_URL = "http://localhost:8080/api";

const AttendeeManagement = () => {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [searchParams] = useSearchParams();
  const initialType = searchParams.get("type") || "ALL";
  const [filterType, setFilterType] = useState(initialType);
  const [selectedEventId, setSelectedEventId] = useState(null);
  const [sessionsForEvent, setSessionsForEvent] = useState([]);
  const [showSessionModal, setShowSessionModal] = useState(false);
  const [selectedSessionId, setSelectedSessionId] = useState(null);
  const [recommendedEvents, setRecommendedEvents] = useState([]);
  const userId = localStorage.getItem("userId");
  
  useEffect(() => {
    fetchAllEvents();
    fetchRecommendedEvents();
  }, []);
  
  useEffect(() => {
    if (filterType === "ALL") {
      setFilteredEvents(events);
    } else if (filterType === "RECOMMENDED") {
      setFilteredEvents(recommendedEvents);
    } else {
      setFilteredEvents(events.filter((event) => event.type === filterType));
    }
  }, [filterType, events, recommendedEvents]);
  
  

  const fetchAllEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/event/all-events`);
      setEvents(res.data);
      setFilteredEvents(res.data); // default to all
    } catch (err) {
      console.error("Failed to fetch events:", err);
    }
  };

  const fetchRecommendedEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/event/recommended-events/${userId}`);
      setRecommendedEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch recommended events:", err);
    }
  };

  const handleFilterChange = (e) => {
    const selectedType = e.target.value;
    setFilterType(selectedType);
  
    if (selectedType === "ALL") {
      setFilteredEvents(events);
    } else if (selectedType === "RECOMMENDED") {
      setFilteredEvents(recommendedEvents);
    } else {
      const filtered = events.filter((event) => event.type === selectedType);
      setFilteredEvents(filtered);
    }
  };  

  const handleRegister = async (eventId) => {
    const userId = localStorage.getItem("userId");
    const role = localStorage.getItem("role");
  
    if (!userId || role !== "ATTENDEE") {
      const confirmLogin = window.confirm("You must be logged in as an attendee to register. Go to login?");
      if (confirmLogin) window.location.href = "/login";
      return;
    }
  
    try {
      const res = await axios.get(`${API_URL}/event/${eventId}/schedules`);
      console.log("Schedules API response:", res.data);
      const schedules = res.data;
  
      const sessionPromises = schedules.map(schedule =>
        axios.get(`${API_URL}/schedule/${schedule.id}/sessions`)
      );
      const sessionResults = await Promise.all(sessionPromises);
      const allSessions = sessionResults.flatMap(res => res.data);
  
      setSessionsForEvent(allSessions);
      setSelectedEventId(eventId);
      setShowSessionModal(true);
    } catch (err) {
      console.error("Failed to load sessions for event:", err);
    }
  };
  
  const checkout = async () => {
    const userEmail = localStorage.getItem("email");
    if (!selectedSessionId || !userEmail) return;
  
    try {

      console.log("Sending to Stripe:", {
        userEmail,
        sessionId: Number(selectedSessionId),
      });
      

      const response = await axios.post("http://localhost:8080/api/product/v1/checkout", {
        userEmail,
        sessionId: Number(selectedSessionId),
      });
  
      // Redirect to Stripe Checkout session
      if (response.data && response.data.sessionUrl) {
        window.location.href = response.data.sessionUrl;
      } else {
        alert("Checkout failed. No URL returned.");
      }
    } catch (err) {
      console.error("Checkout error:", err);
      alert("Checkout failed. Please try again.");
    }
  };
  

  return (
    <div className="min-h-screen flex flex-col items-center mt-6">
      <h2 className="mt-8 text-2xl font-bold pt-2">Browse All Events</h2>
      <p className="text-gray-600 mt-2">
        Discover all upcoming educational events across seminars, webinars, workshops, and conferences.
      </p>

      {/* Decorative Divider */}
      <hr className="my-10 w-full max-w-6xl border-t border-gray-300" />


      {/* Filter Tabs */}
      <div className="flex space-x-6 overflow-x-auto mb-6 w-full max-w-6xl px-4">
        {["ALL", "SEMINAR", "WEBINAR", "WORKSHOP", "CONFERENCE", "RECOMMENDED"].map((type) => (
          <button
            key={type}
            onClick={() => handleFilterChange({ target: { value: type } })}
            className={`whitespace-nowrap pb-2 font-medium border-b-2 transition-all duration-300 ${
              filterType === type
                ? "text-[#8B5E3C] border-[#8B5E3C]"
                : "text-gray-600 border-transparent hover:text-black hover:border-black"
            }`}
          >
            {type === "ALL"
              ? "All"
              : type === "RECOMMENDED"
              ? "Recommended"
              : (type.charAt(0) + type.slice(1).toLowerCase()) + "s"}
                  </button>
        ))}
      </div>


      {/* Event Grid */}
      {filteredEvents.length > 0 ? (
        <div className="w-full max-w-6xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {filteredEvents.map((event) => (
            <div key={event.id} className="border border-[#D9C2A3] bg-white p-4 rounded-lg shadow flex flex-col justify-between">
              <h3 className="font-bold text-lg text-[#2E2E2E]">{event.name}</h3>
              <p className="text-sm text-gray-700 mt-1">{event.description}</p>
              <p className="text-sm mt-2"><strong>Type:</strong> {event.type}</p>
              <p className="text-sm text-gray-500">Price: ${event.price}</p>
              <button
                onClick={() => handleRegister(event.id)}
                className="mt-3 bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded hover:bg-[#C4A88E] transition"
              >
                Register
              </button>

            </div>
          ))}
        </div>
      ) : (
        <p className="text-gray-500 mt-4">No events found.</p>
      )}

      {showSessionModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded shadow-lg w-96">
            <h3 className="text-lg font-bold mb-4">Choose a Session</h3>
            <select
              className="w-full border p-2 mb-4"
              value={selectedSessionId || ""}
              onChange={(e) => setSelectedSessionId(e.target.value)}
            >
              <option value="">Select a session</option>
              {sessionsForEvent.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.title} ({s.startTime} - {s.endTime}) @ {s.location}
                </option>
              ))}
            </select>
            <div className="flex justify-end gap-2">
              <button
                onClick={() => setShowSessionModal(false)}
                className="bg-gray-300 px-3 py-1 rounded"
              >
                Cancel
              </button>
              <button
                onClick={checkout}
                className="bg-[#D9C2A3] px-3 py-1 rounded hover:bg-[#C4A88E]"
              >
                Checkout
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default AttendeeManagement;
