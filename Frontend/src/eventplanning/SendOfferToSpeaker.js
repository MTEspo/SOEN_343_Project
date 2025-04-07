// ReachOutToSpeakers.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from 'react-router-dom';


const API_URL = "http://localhost:8080/api";

const ReachOutToSpeakers = ( ) => {
  const [availableSpeakers, setAvailableSpeakers] = useState([]);
  const [loading, setLoading] = useState(false);
  const { sessionId } = useParams();
  const organizerId = localStorage.getItem("userId");


  const fetchAvailableSpeakers = async () => {
    try {
      console.log(sessionId)
      const res = await axios.get(`${API_URL}/session/${sessionId}/eligible-speakers`);
      setAvailableSpeakers(res.data);
    } catch (err) {
      console.error("Failed to fetch available speakers:", err);
    }
  };

  const handleSendOffer = async (speakerId) => {
    setLoading(true);
    try {
      await axios.post(`${API_URL}/organizer/create-speaker-offer`, {
        speakerId,
        sessionId,
        organizerId
      });
      alert("Offer sent successfully!");
    } catch (err) {
      console.error("Failed to send offer:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAvailableSpeakers();
  }, [sessionId]);

  return (
    <div className="mt-4">
      <h2 className="text-2xl font-bold">Reach out to speakers</h2>
      <p className="text-gray-600 mt-2">Select a speaker to send an offer:</p>

      {availableSpeakers.length > 0 ? (
        <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {availableSpeakers.map((speaker) => (
            <div key={speaker.id} className="border border-[#D9C2A3] p-4 rounded-lg shadow bg-white flex flex-col justify-between">
              <h3 className="font-bold text-lg">{speaker.username}</h3>
              <p className="text-sm text-gray-700">Email: {speaker.email}</p>
              <p className="text-sm text-gray-700">Average Rating: {speaker.averageRating}</p>

              <button
                className="bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]"
                onClick={() => handleSendOffer(speaker.id)}
                disabled={loading}
              >
                {loading ? "Sending..." : "Send Offer"}
              </button>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-gray-500 mt-4">No available speakers found.</p>
      )}
    </div>
  );
};

export default ReachOutToSpeakers;