import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

const API_URL = "http://localhost:8080/api/";

const EventsAnalyticsPage = () => {
  const { eventId } = useParams();
  const [analytics, setAnalytics] = useState(null);

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const res = await axios.get(`${API_URL}event/${eventId}/get-event-analytics`);
        setAnalytics(res.data);
      } catch (err) {
        console.error("Failed to fetch event analytics:", err);
      }
    };
    fetchAnalytics();
  }, [eventId]);

  if (!analytics) return <div className="text-center mt-10 text-gray-500">Loading...</div>;

  return (
    <div className="flex flex-col min-h-screen">
      {/* Main content */}
      <main className="flex-grow px-6 py-8 max-w-5xl mx-auto">
        <h1 className="text-3xl font-bold text-center mb-6 text-[#2E2E2E]">Event Analytics</h1>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 mb-10">
          <div className="p-4 bg-white shadow-md border border-[#D9C2A3] rounded-lg">
            <h2 className="text-lg font-semibold text-[#5A5958]">Total Amount Generated</h2>
            <p className="text-xl font-bold text-[#2E2E2E]">${analytics.amountGenerated}</p>
          </div>

          <div className="p-4 bg-white shadow-md border border-[#D9C2A3] rounded-lg">
            <h2 className="text-lg font-semibold text-[#5A5958]">Number of Speakers</h2>
            <p className="text-xl font-bold text-[#2E2E2E]">{analytics.speakers.length}</p>
          </div>

          <div className="p-4 bg-white shadow-md border border-[#D9C2A3] rounded-lg">
            <h2 className="text-lg font-semibold text-[#5A5958]">Number of Attendees</h2>
            <p className="text-xl font-bold text-[#2E2E2E]">{analytics.attendees.length}</p>
          </div>
        </div>

        {/* Speakers List */}
        <div className="mb-10">
          <h2 className="text-2xl font-bold mb-4 text-[#2E2E2E]">Speakers</h2>
          {analytics.speakers.length === 0 ? (
            <p className="text-gray-500">No speakers registered for this event.</p>
          ) : (
            <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
              {analytics.speakers.map((speaker) => (
                <li key={speaker.id} className="bg-white border border-[#D9C2A3] p-4 rounded-lg shadow">
                  <p><span className="font-semibold">Email:</span> {speaker.email}</p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </main>
    </div>
  );
};

export default EventsAnalyticsPage;
