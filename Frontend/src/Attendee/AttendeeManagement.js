import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api";

const AttendeeManagement = () => {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    fetchAllEvents();
  }, []);

  const fetchAllEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/event/all-events`);
      setEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch events:", err);
    }
  };

  return (
    <div className="flex flex-col items-center mt-6">
      <h2 className="text-2xl font-bold pt-2">Browse All Events</h2>
      <p className="text-gray-600 mt-2">
        Discover all upcoming educational events across seminars, webinars, workshops, and conferences.
      </p>

      {/* Future: Registered Events */}
      {/* <div className="w-full max-w-6xl mb-8">
        <h3 className="text-xl font-semibold mb-2">Your Registered Events</h3>
        // Will map registered events here later
      </div> */}

      {/* Decorative Divider */}
      <hr className="my-10 w-full max-w-6xl border-t border-gray-300" />

      {/* All Events Grid */}
      {events.length > 0 ? (
        <div className="w-full max-w-6xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {events.map((event) => (
            <div key={event.id} className="border border-[#D9C2A3] bg-white p-4 rounded-lg shadow flex flex-col justify-between">
              <h3 className="font-bold text-lg text-[#2E2E2E]">{event.name}</h3>
              <p className="text-sm text-gray-700 mt-1">{event.description}</p>
              <p className="text-sm mt-2"><strong>Type:</strong> {event.type}</p>
              <p className="text-sm text-gray-500">Price: ${event.price}</p>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-gray-500 mt-4">No events found.</p>
      )}
    </div>
  );
};

export default AttendeeManagement;
