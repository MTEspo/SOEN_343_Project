import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import { EventService } from "./observer/Subject";
import { EmailNotifier } from "./observer/EmailNotifier";

const API_URL = "http://localhost:8080/api";

const Promotion = () => {
  const [events, setEvents] = useState([]);

  // Using useRef to keep observer service instance between renders
  const eventServiceRef = useRef(new EventService());

  
  const token = localStorage.getItem("token");
  const organizerId = localStorage.getItem("userId");

  useEffect(() => {
    // Register all observers once on mount
    const service = eventServiceRef.current;
    service.addObserver(new EmailNotifier());
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/organizer/${organizerId}/events`);
      setEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch events:", err);
    }
  };

  const handlePromote = (event) => {
    eventServiceRef.current.promoteEvent(event);
    alert(`Promotion triggered for ${event.name}!`);
  };

  return (
    <div className="min-h-screen flex flex-col p-8">
      <h1 className="text-4xl font-bold text-center text-[#2E2E2E] mb-4">
        Event Promotions
      </h1>
      <p className="text-center text-[#5A5958] mb-10">
        Review your published events and trigger promotions.
      </p>

      {events.length === 0 ? (
        <p className="text-center text-gray-500">No events to promote yet.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {events.map((event) => (
            <div key={event.id} className="bg-white border border-[#D9C2A3] p-4 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-1">{event.name}</h2>
              <p className="text-sm text-gray-600">{event.description}</p>
              <div className="mt-3">
                <p className="text-sm"><strong>Type:</strong> {event.type}</p>
                <p className="text-sm"><strong>Price:</strong> ${parseFloat(event.price).toFixed(2)}</p>
              </div>
              <button
                onClick={() => handlePromote(event)}
                className="mt-4 w-full bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]"
              >
                Promote
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Promotion;
