import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { FaDownload } from 'react-icons/fa';


const API_URL = "http://localhost:8080/api";

const SessionDetails = () => {
    const { sessionId } = useParams();
    const [sessionDetails, setSessionDetails] = useState(null);
    const [eventDetails, setEventDetails] = useState(null);
    const [eventResources, setEventResources] = useState([]);
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

    useEffect(() => {
        const fetchEventDetails = async () => {
            if (sessionDetails) {
                try {
                    const response = await axios.get(`${API_URL}/session/${sessionId}/get-event`);
                    setEventDetails(response.data);
                } catch (error) {
                    console.error(error);
                }
            }
        };
        fetchEventDetails();
    }, [sessionDetails]);

    useEffect(() => {
        const fetchEventResources = async () => {
            if (eventDetails) {
                try {
                    const response = await axios.get(`${API_URL}/event/${eventDetails.id}/resources`);
                    setEventResources(response.data);
                } catch (error) {
                    console.error(error);
                }
            }
        };
        fetchEventResources();
    }, [eventDetails]);

    if (!sessionDetails || !eventDetails) return <p>Loading...</p>;

    const handleEnterChatroom = () => {
        navigate(`/chat/${sessionDetails.chatroomId}`);
    };

    const handleDownloadResource = async (resourceId) => {
      try {
          const response = await axios.get(
              `${API_URL}/event/${eventDetails.id}/resources/${resourceId}/download`,
              { responseType: 'blob' }
          );
  
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
  
          // Get Content-Disposition header safely
          const contentDisposition = response.headers['content-disposition'];
          let fileName = 'downloaded-file';
  
          if (contentDisposition && contentDisposition.includes('filename=')) {
              try {
                  fileName = contentDisposition.split('filename="')[1].split('"')[0];
              } catch (err) {
                  console.warn("Failed to parse filename from Content-Disposition header:", err);
              }
          }
  
          link.setAttribute('download', fileName);
          document.body.appendChild(link); // optional for Safari support
          link.click();
          link.remove(); // cleanup
  
      } catch (error) {
          console.error("Download failed:", error);
      }
  };
  

  return (
    <div className="flex justify-center items-center min-h-screen bg-[#f7f7f7] p-6">
        <div className="max-w-4xl w-full bg-white shadow-lg rounded-xl p-8">
            <h1 className="text-4xl font-extrabold text-center mb-6">{eventDetails.name}</h1>
            <div className="text-lg text-gray-700 space-y-4">
                <p><span className="font-semibold">Description:</span> {eventDetails.description}</p>
                <p><span className="font-semibold">Type:</span> {eventDetails.type}</p>
            </div>
            <h2 className="text-3xl font-bold mt-6 mb-4">Session Details</h2>
            <div className="text-lg text-gray-700 space-y-4">
                <p><span className="font-semibold">Location:</span> {sessionDetails.location}</p>
                <p><span className="font-semibold">Time:</span> {sessionDetails.startTime} - {sessionDetails.endTime}</p>
                <p><span className="font-semibold">Speaker:</span> {sessionDetails.speakerUsername}</p>
                <p><span className="font-semibold">Date:</span> {sessionDetails.scheduleDate}</p>
            </div>
            <h2 className="text-3xl font-bold mt-6 mb-4">Resources</h2>
            {eventResources.length > 0 ? (
                <ul>
                    {eventResources.map((resource) => (
                        <li key={resource.id}>
                            <span className="font-semibold">{resource.name}</span>
                            <button 
                                className="ml-2 px-2 py-1 bg-[#5A5958] text-white rounded-md text-sm font-semibold hover:bg-[#3E3D3C] transition"
                                onClick={() => handleDownloadResource(resource.id)}
                            >
                                <FaDownload />
                            </button>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No resources available.</p>
            )}
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