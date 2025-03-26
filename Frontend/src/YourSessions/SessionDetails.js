import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

const API_URL = "http://localhost:8080/api";

const SessionDetails = () => {
    const { sessionId } = useParams();
    const [sessionDetails, setSessionDetails] = useState(null);
    const navigate = useNavigate();
  
    useEffect(() => {
      const fetchSessionDetails = async () => {
        try {
          const response = await axios.get(`${API_URL}/session/${sessionId}`);
          setSessionDetails(response.data);
        } catch (error) {
          console.error(error);
        }
      };
      fetchSessionDetails();
    }, [sessionId]);

    if (!sessionDetails) return <p>Loading...</p>;

    const handleEnterChatroom = () => {
        navigate(`/chat/${sessionDetails.chatroomId}`);
    };
  
    return (
        <div className="flex justify-center items-center min-h-screen bg-[#f7f7f7] p-6">
          <div className="max-w-4xl w-full bg-white shadow-lg rounded-xl p-8">
            <h1 className="text-4xl font-extrabold text-center mb-6">{sessionDetails.title}</h1>
            <div className="text-lg text-gray-700 space-y-4">
              <p><span className="font-semibold">Location:</span> {sessionDetails.location}</p>
              <p><span className="font-semibold">Time:</span> {sessionDetails.startTime} - {sessionDetails.endTime}</p>
              <p><span className="font-semibold">Speaker:</span> {sessionDetails.speakerUsername}</p>
              <p><span className="font-semibold">Date:</span> {sessionDetails.scheduleDate}</p>
            </div>
            <button
                onClick={handleEnterChatroom}
                className="mt-6 px-6 py-3 bg-[#5A5958] text-white rounded-md text-lg font-semibold hover:bg-[#3E3D3C] transition"
                >
                Enter Chatroom
            </button>
          </div>
        </div>
      );
  };
  
  export default SessionDetails;
