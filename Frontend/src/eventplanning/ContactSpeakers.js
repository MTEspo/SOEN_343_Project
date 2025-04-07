import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from 'react-router-dom';

const API_URL = "http://localhost:8080/api";

const ContactSpeakers = () => {
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [schedules, setSchedules] = useState([]);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(false);
  const organizerId = localStorage.getItem("userId");
   const { eventId } = useParams();

  const fetchEvent = async () => {
    try {
      const res = await axios.get(`${API_URL}/event/${eventId}`);
      setSelectedEvent(res.data);
    } catch (err) {
      console.error("Failed to fetch event:", err);
    }
  };

  const fetchSchedules = async () => {
    try {
      const res = await axios.get(`${API_URL}/event/${eventId}/schedules`);
      setSchedules(res.data);
    } catch (err) {
      console.error("Failed to fetch schedules:", err);
    }
  };

  const fetchSessions = async (scheduleId) => {
    try {
      const res = await axios.get(`${API_URL}/schedule/${scheduleId}/sessions`);
      setSessions(res.data);
    } catch (err) {
      console.error("Failed to fetch sessions:", err);
    }
  };

  useEffect(() => {
    fetchEvent();
    fetchSchedules();
  }, []);

  const handleScheduleSelect = (scheduleId) => {
    setSelectedSchedule(scheduleId);
    fetchSessions(scheduleId);
  };

  return (
    <div className="mt-4">
      <h2 className="text-2xl font-bold">Contact Speakers</h2>
      <p className="text-gray-600 mt-2">Select a schedule and session to contact speakers:</p>

      {selectedEvent && (
        <div className="mt-4">
          <h3 className="font-bold text-lg">Schedules for {selectedEvent.name}:</h3>
          {schedules.length > 0 ? (
            <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
              {schedules.map((schedule) => {
                    const date = new Date(schedule.date);
                    const dayOfWeek = date.toLocaleString('en-US', { weekday: 'long' });
                    const dayOfMonth = date.getDate();
                    const month = date.toLocaleString('en-US', { month: 'long' });
                    const year = date.getFullYear();

                    return (
                        <div key={schedule.id} className="border border-[#D9C2A3] p-4 rounded-lg shadow bg-white flex flex-col justify-between">
                        <h3 className="font-bold text-lg">{dayOfWeek}, {dayOfMonth} {month} {year}</h3>
                        <button
                            className="bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]"
                            onClick={() => handleScheduleSelect(schedule.id)}
                        >
                            Select Schedule
                        </button>
                        </div>
                    );
                    })}
            </div>
          ) : (
            <p className="text-gray-500 mt-4">No schedules found.</p>
          )}
        </div>
      )}

      {selectedSchedule && (
        <div className="mt-4">
          <h3 className="font-bold text-lg">Sessions for {schedules.find((schedule) => schedule.id === selectedSchedule).name}:</h3>
          {sessions.length > 0 ? (
            <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
              {sessions.map((session) => (
                <div key={session.id} className="border border-[#D9C2A3] p-4 rounded-lg shadow bg-white flex flex-col justify-between">
                  <h3 className="font-bold text-lg">{session.name}</h3>
                  <p className="text-sm text-gray-700">Session ID: {session.id}</p>
                  <button
                    className="bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]"
                    onClick={() => {
                      window.location.href = `/reach-out-to-speakers/${session.id}`;
                    }}
                  >
                    Contact Speakers
                  </button>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-gray-500 mt-4">No sessions found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default ContactSpeakers;