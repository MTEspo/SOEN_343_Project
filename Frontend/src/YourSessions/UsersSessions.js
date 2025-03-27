import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
const API_URL = "http://localhost:8080/api";
const SessionsPage = () => {
  const [sessions, setSessions] = useState([]);
  const userId = localStorage.getItem("userId");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchSessions = async () => {
      try {
        console.log(userId)
        const response = await axios.get(`${API_URL}/tickets/get-all-users-sessions/${userId}`);
        setSessions(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchSessions();
  }, [userId]);

  const handleAccessDetails = (sessionId) => {
    navigate(`/session-details/${sessionId}`);
  };

  useEffect(() => {
    console.log("Updated Sessions State:", sessions);
  }, [sessions]);

  return (
    <div className="min-h-screen flex flex-col items-center mt-6">
      <h2 className="text-2xl font-bold pt-2">Your Sessions</h2>
      <p className="text-gray-600 mt-2">
        Here are the sessions you have access to:
      </p>

      <hr className="my-10 w-full max-w-6xl border-t border-gray-300" />

      {sessions.length > 0 ? (
        <div className="w-full max-w-6xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {sessions.map((session) => (
  <div key={session.id} className="border border-[#D9C2A3] bg-white p-4 rounded-lg shadow flex flex-col justify-between">
    <h3 className="font-bold text-lg text-[#2E2E2E]">{session.title}</h3>
    <p className="text-sm text-gray-700 mt-1">
      {session.location} | {session.startTime} - {session.endTime}
    </p>
    <button
                className="mt-2 bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded-md hover:bg-[#C4A88E] transition"
                onClick={() => handleAccessDetails(session.id)}
              >
                Access Details
              </button>
  </div>
))}
        </div>
      ) : (
        <p className="text-gray-500 mt-4">No sessions found.</p>
      )}
    </div>
  );
};

export default SessionsPage;