import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const API_URL = "http://localhost:8080/api/";

const StakeholderAnalytics = () => {
  const [events, setEvents] = useState([]);
  const stakeholderId = localStorage.getItem("userId");
  const navigate = useNavigate();

  useEffect(() => {
    fetchInvestedEvents();
  }, []);

  const fetchInvestedEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}stakeholders/${stakeholderId}/invested-events`);
      
      setEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch invested events:", err);
    }
  };

  const handleAccessAnalytics = (eventId) => {
    console.log("Event ID being passed:", eventId);
    navigate(`/dropdowns/stakeholder/StakeholderAnalyticsDetails/${eventId}`);
  };

  return (
    <div className="flex flex-col min-h-screen p-8">
      <h1 className="text-4xl font-bold text-center text-[#2E2E2E] mb-4">Your Event Investments</h1>
      <p className="text-center text-[#5A5958] mb-10">See analytics for events you've invested in.</p>

      {events.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {events.map((event) => (
            <div key={event.eventId} className="bg-white border border-[#D9C2A3] p-4 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-1">{event.eventName}</h2>
              <p className="text-sm text-gray-600">{event.description}</p>
              <div className="mt-3">
                <p className="text-sm"><strong>Funding Goal:</strong> ${parseFloat(event.fundingGoal).toFixed(2)}</p>
              </div>
              <button
                onClick={() => handleAccessAnalytics(event.eventId)}
                className="mt-4 w-full bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition hover:bg-[#C4A88E]"
              >
                Access Analytics
              </button>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-center text-lg">You haven't invested in any events yet.</p>
      )}
    </div>
  );
};

export default StakeholderAnalytics;