import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api";

const EventPlanning = () => {
  const [events, setEvents] = useState([]);
  const [schedules, setSchedules] = useState([]);
  const [eventSchedules, setEventSchedules] = useState({});

  const [newEvent, setNewEvent] = useState({ name: "", description: "", price: "", type: "" });
  const [newSchedule, setNewSchedule] = useState({ date: "", eventId: null });
  const [newSession, setNewSession] = useState({ title: "", startTime: "", endTime: "", location: "", scheduleId: "" });

  const [openForm, setOpenForm] = useState({ eventId: null, type: null });

  useEffect(() => {
    fetchEvents();
    fetchAllSchedules();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/event`);
      setEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch events:", err);
    }
  };

  const fetchAllSchedules = async () => {
    try {
      const res = await axios.get(`${API_URL}/schedule`);
      setSchedules(res.data);
    } catch (err) {
      console.error("Failed to fetch all schedules:", err);
    }
  };
  

  const fetchSchedulesForEvent = async (eventId) => {
    try {
      const res = await axios.get(`${API_URL}/schedule`);
      const allSchedules = res.data;
  
      // Fetch event ID for each schedule and filter
      const matchingSchedules = [];
  
      for (const schedule of allSchedules) {
        try {
          const eventRes = await axios.get(`${API_URL}/schedule/${schedule.id}/event`);
          if (eventRes.data === eventId) {
            matchingSchedules.push(schedule);
          }
        } catch (err) {
          console.error(`Error getting event ID for schedule ${schedule.id}`, err);
        }
      }
  
      setSchedules(matchingSchedules);
    } catch (err) {
      console.error("Failed to fetch schedules:", err);
    }
  };
    
  const handleCreateEvent = async (e) => {
    e.preventDefault();
    if (!newEvent.type) return;

    try {
      const res = await axios.post(`${API_URL}/event/create`, newEvent);
      setEvents([...events, res.data]);
      setNewEvent({ name: "", description: "", price: "", type: "" });
    } catch (err) {
      console.error("Failed to create event:", err);
    }
  };

  const handleCreateSchedule = async (e) => {
    e.preventDefault();
    if (!newSchedule.date || !newSchedule.eventId) return;

    try {
      const res = await axios.post(`${API_URL}/schedule/create/${newSchedule.eventId}`, newSchedule);
      setSchedules([...schedules, res.data]);
      setNewSchedule({ date: "", eventId: null });
      setOpenForm({ eventId: null, type: null });
    } catch (err) {
      console.error("Failed to create schedule:", err);
    }
  };

  const handleCreateSession = async (e) => {
    e.preventDefault();
    if (!newSession.scheduleId) return;

    try {
      const res = await axios.post(`${API_URL}/session/create/${newSession.scheduleId}`, newSession);
      setNewSession({ title: "", startTime: "", endTime: "", location: "", scheduleId: "" });
      setOpenForm({ eventId: null, type: null });
    } catch (err) {
      console.error("Failed to create session:", err);
    }
  };

  return (
    <div className="flex flex-col items-center mt-6">
      <h2 className="text-2xl font-bold pt-2">Event Planning</h2>
      <p className="text-gray-600 mt-2">Manage your events efficiently.</p>

      {/* Display Events in Grid Format */}
      {events.length > 0 && (
        <div className="mt-8 w-full max-w-6xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
          {events.map((event) => (
            <div key={event.id} className="border p-4 rounded-lg shadow bg-white flex flex-col justify-between">
              <div>
                <h3 className="font-bold text-lg">{event.name}</h3>
                <p className="text-sm text-gray-700">{event.description}</p>
                <p className="text-sm mt-1"><strong>Type:</strong> {event.type}</p>
                <p className="text-sm text-gray-500">Price: ${event.price}</p>
              </div>

              <div className="flex space-x-2 mt-3">
                {/* ADD SCHEDULE BUTTON*/}
                <button
                  className="bg-[#D9C2A3] text-[#2E2E2E] px-3 py-1 rounded transition duration-300 hover:bg-[#C4A88E]"
                  onClick={() => {
                    setOpenForm({ eventId: event.id, type: 'schedule' });
                    setNewSchedule({ date: "", eventId: event.id });
                  }}
                >
                  Add Schedule
                </button>

                {/* ADD SESSION BUTTON*/}
                <button
                  className="bg-[#D9C2A3] text-[#2E2E2E] px-3 py-1 rounded transition duration-300 hover:bg-[#C4A88E]"
                  onClick={async () => {
                    await fetchSchedulesForEvent(event.id); 
                    setOpenForm({ eventId: event.id, type: 'session' });
                  }}
                >
                  Add Session
                </button>
              </div>

              {/* SCHEDULE FORM */}
              {openForm.eventId === event.id && openForm.type === 'schedule' && (
                <form onSubmit={handleCreateSchedule} className="mt-4 flex flex-col gap-2">
                  <input
                    type="date"
                    value={newSchedule.date}
                    onChange={(e) => setNewSchedule({ ...newSchedule, date: e.target.value })}
                    className="border p-2 rounded"
                    required
                  />
                  <button type="submit" className="bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]">Save Schedule</button>
                </form>
              )}

              {/* SESSION FORM */}
              {openForm.eventId === event.id && openForm.type === 'session' && (
                <form onSubmit={handleCreateSession} className="mt-4 flex flex-col gap-2">

                  {/* SCHEDULE SELECTION */}
                  <select
                    value={newSession.scheduleId}
                    onChange={(e) => setNewSession({ ...newSession, scheduleId: e.target.value })}
                    className="border p-2 rounded"
                    required
                  >
                    <option value="">Select Schedule</option>
                    {schedules.map((s) => (
                      <option key={s.id} value={s.id}>{s.date}</option>
                    ))}
                  </select>
                  <input
                    type="text"
                    placeholder="Title"
                    value={newSession.title}
                    onChange={(e) => setNewSession({ ...newSession, title: e.target.value })}
                    className="border p-2 rounded"
                    required
                  />
                  <input
                    type="time"
                    placeholder="Start Time"
                    value={newSession.startTime}
                    onChange={(e) => setNewSession({ ...newSession, startTime: e.target.value })}
                    className="border p-2 rounded"
                    required
                  />
                  <input
                    type="time"
                    placeholder="End Time"
                    value={newSession.endTime}
                    onChange={(e) => setNewSession({ ...newSession, endTime: e.target.value })}
                    className="border p-2 rounded"
                    required
                  />
                  <input
                    type="text"
                    placeholder="Location"
                    value={newSession.location}
                    onChange={(e) => setNewSession({ ...newSession, location: e.target.value })}
                    className="border p-2 rounded"
                    required
                  />
                  <button type="submit" className="bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]">Save Session</button>
                </form>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Decorative Line */}
      <hr className="my-10 w-full max-w-6xl border-t border-gray-300" />

      {/* Event Creation Form */}
      <h2 className="text-2xl font-bold pt-2">Event Creation</h2>
      <p className="text-gray-600 mt-2">Create your event in a few clicks!</p>

      <form onSubmit={handleCreateEvent} className="mt-4 flex flex-col gap-2 w-1/2">
        <input
          type="text"
          placeholder="Event Name"
          value={newEvent.name}
          onChange={(e) => setNewEvent({ ...newEvent, name: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <input
          type="text"
          placeholder="Description"
          value={newEvent.description}
          onChange={(e) => setNewEvent({ ...newEvent, description: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <select
          value={newEvent.type}
          onChange={(e) => setNewEvent({ ...newEvent, type: e.target.value })}
          className="border p-2 rounded"
          required
        >
          <option value="">Event type</option>
          <option value="SEMINAR">SEMINAR</option>
          <option value="WEBINAR">WEBINAR</option>
          <option value="WORKSHOP">WORKSHOP</option>
          <option value="CONFERENCE">CONFERENCE</option>
        </select>
        <input
          type="number"
          placeholder="Price"
          value={newEvent.price}
          onChange={(e) => setNewEvent({ ...newEvent, price: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <button type="submit" className="bg-[#D9C2A3] text-[#2E2E2E] p-2 rounded transition duration-300 hover:bg-[#C4A88E]">Create Event</button>
      </form>
    </div>
  );
};

export default EventPlanning;
