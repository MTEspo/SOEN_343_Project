import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
const API_URL = "http://localhost:8080/api/";

const GetAnalyticsPage = () => {
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

  const handleAccessAnalytics = async (eventId) => {
    navigate(`/dropdowns/organizer/EventsAnalyticsDetails/${eventId}`);
};

  return (
    <div>
      {events.length > 0 ? (
        <div className="mt-8 w-full max-w-6xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
          {events.map((event) => (
            <div
              key={event.id}
              className="border border-[#D9C2A3] p-6 rounded-lg shadow bg-white flex flex-col justify-between"
              style={{ width: '400px' }}
            >
              <h3 className="font-bold text-lg">{event.name}</h3>
              <p className="text-sm text-gray-700">{event.description}</p>
              <p className="text-sm mt-1"><strong>Type:</strong> {event.type}</p>
              <p className="text-sm text-gray-500">Price: ${event.price}</p>
              <button
                className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 mt-4"
                onClick={() => handleAccessAnalytics(event.id)}
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
  );
};

export default GetAnalyticsPage;