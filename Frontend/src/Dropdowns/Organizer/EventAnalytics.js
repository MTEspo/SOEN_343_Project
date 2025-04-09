import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
const API_URL = "http://localhost:8080/api/";

const EventAnalytics = () => {
  const [events, setEvents] = useState([]);
  const organizerId = localStorage.getItem("userId");
  const navigate = useNavigate();

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}organizer/${organizerId}/events`);
      setEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch events:", err);
    }
  };

  const handleAccessAnalytics = (eventId) => {
    navigate(`/dropdowns/organizer/EventsAnalyticsDetails/${eventId}`);
  };

  return (
    <div className="flex flex-col min-h-screen">
      {/* Main Content */}
      <div className="flex-grow flex flex-col p-8">
        <h1 className="text-4xl font-bold text-center text-[#2E2E2E] mb-4">
          Event Analytics
        </h1>
        <p className="text-center text-[#5A5958] mb-10">
          View detailed analytics for your published events.
        </p>

        {events.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
            {events.map((event) => (
              <div
                key={event.id}
                className="bg-white border border-[#D9C2A3] p-4 rounded-lg shadow h-auto"
              >
                <h2 className="text-xl font-bold mb-1">{event.name}</h2>
                <p className="text-sm text-gray-600">{event.description}</p>
                <div className="mt-3">
                  <p className="text-sm">
                    <strong>Type:</strong> {event.type}
                  </p>
                  <p className="text-sm">
                    <strong>Price:</strong> ${parseFloat(event.price).toFixed(2)}
                  </p>
                </div>
                <button
                  onClick={() => handleAccessAnalytics(event.id)}
                  className="mt-4 w-full bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]"
                >
                  Access Analytics
                </button>
              </div>
            ))}
          </div>
        ) : (
          <div className="mt-8 text-center text-lg">
            <p>You currently have no events.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default EventAnalytics;
