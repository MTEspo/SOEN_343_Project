import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api";

const StakeholderInvestPage = () => {
  const [events, setEvents] = useState([]);
  const stakeholderId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const res = await axios.get(`${API_URL}/event/all-events`);
        setEvents(res.data);
      } catch (err) {
        console.error("Failed to fetch events", err);
      }
    };

    fetchEvents();
  }, []);

  const handleInvest = async (eventId) => {
    const amount = prompt("Enter investment amount:");
    if (!amount || isNaN(amount) || parseFloat(amount) <= 0) {
      alert("Invalid amount.");
      return;
    }

    try {
        axios.post(
            `http://localhost:8080/api/stakeholders/${stakeholderId}/invest/${eventId}`,
            null, // no request body
            {
              params: {
                amount: amount,
              }
            }
          ); alert(`Thank you for your investment of $${amount}! 🎉`);
      } catch (err) {
        console.error("Investment failed", err);
        alert("Investment failed.");
      }
  };

  return (
    <div className="min-h-screen p-6 bg-[#F5F2EC]">
      <h2 className="text-2xl font-bold text-center mb-6">Available Events for Investment</h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {events.map((event) => (
          <div key={event.id} className="bg-white p-4 rounded shadow border border-[#D9C2A3]">
            <h3 className="text-xl font-semibold">{event.name}</h3>
            <p className="mt-2">Funding Goal: ${parseFloat(event.fundingGoal).toFixed(2)}</p>
            <button
              onClick={() => handleInvest(event.id)}
              className="mt-4 w-full bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded hover:bg-[#C4A88E]"
            >
              Invest Now
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default StakeholderInvestPage;