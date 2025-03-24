import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api";

const EventPlanning = () => {
  const [events, setEvents] = useState([]);
  const [schedules, setSchedules] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [eventSchedules, setEventSchedules] = useState({});
  const [eventSessions, setEventSessions] = useState({});
  const [editingEventId, setEditingEventId] = useState(null);
  const [editFields, setEditFields] = useState({ name: "", description: "", price: "" });

  const [newEvent, setNewEvent] = useState({ name: "", description: "", price: "", type: "" });
  const [newSchedule, setNewSchedule] = useState({ date: "", eventId: null });
  const [newSession, setNewSession] = useState({ title: "", startTime: "", endTime: "", location: "", scheduleId: "" });

  const [openForm, setOpenForm] = useState({ eventId: null, type: null });

  useEffect(() => {
    fetchEvents();
    fetchAllSchedules();
    fetchAllSessions();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/event/all-events`);
      setEvents(res.data);
      await loadSchedulesAndSessionsForEvents(res.data);
    } catch (err) {
      console.error("Failed to fetch events:", err);
    }
  };

  const fetchAllSchedules = async () => {
    try {
      const res = await axios.get(`${API_URL}/schedule/all-schedules`);
      console.log("All schedules fetched:", res.data);    
      setSchedules(res.data);
    } catch (err) {
      console.error("Failed to fetch all schedules:", err);
    }
  };

  const fetchSchedulesForEvent = async (eventId) => {
    try {
      const res = await axios.get(`${API_URL}/schedule/all-schedules`);
      const allSchedules = res.data;

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

  const fetchAllSessions = async () => {
    try{
      const res = await axios.get(`${API_URL}/session/all-sessions`);
      console.log("All sessions fetched:", res.data);
      setSessions(res.data);
    } catch (err){
      console.error("Failed to fetch all sessions:", err);
    }
  }

  const loadSchedulesAndSessionsForEvents = async () => {
    const eventScheduleMap = {};
    const eventSessionMap = {};
  
    try {
      const schedulesRes = await axios.get(`${API_URL}/schedule/all-schedules`);
      const sessionsRes = await axios.get(`${API_URL}/session/all-sessions`);
      const allSchedules = schedulesRes.data;
      const allSessions = sessionsRes.data;
  
      for (const schedule of allSchedules) {
        const eventRes = await axios.get(`${API_URL}/schedule/${schedule.id}/event`);
        const eventId = eventRes.data;
  
        // Add schedule to the correct event
        if (!eventScheduleMap[eventId]) eventScheduleMap[eventId] = [];
        eventScheduleMap[eventId].push(schedule);
      }
  
      for (const session of allSessions) {
        const scheduleRes = await axios.get(`${API_URL}/session/${session.id}/schedule`);
        const scheduleId = scheduleRes.data;
  
        // Use the scheduleId to find its eventId
        const eventId = Object.keys(eventScheduleMap).find((eid) =>
          eventScheduleMap[eid].some((s) => s.id === scheduleId)
        );
  
        if (!eventId) continue; // skip if schedule isn't mapped
  
        // Add session under the correct event
        if (!eventSessionMap[eventId]) eventSessionMap[eventId] = [];
        eventSessionMap[eventId].push({ ...session, scheduleId });
      }
  
      setEventSchedules(eventScheduleMap);
      setEventSessions(eventSessionMap);
    } catch (err) {
      console.error("Error mapping schedules and sessions:", err);
    }
  };
    
  const handleCreateEvent = async (e) => {
    e.preventDefault();
    if (!newEvent.type) return;

    try {
      const res = await axios.post(`${API_URL}/event/create`, newEvent);
      setEvents([...events, res.data]);
      setNewEvent({ name: "", description: "", price: "", type: "" });
      alert("Event created successfully!");
    } catch (err) {
      console.error("Failed to create event:", err);
    }
  };

  const handleCreateSchedule = async (e) => {
    e.preventDefault();
    if (!newSchedule.date || !newSchedule.eventId) return;

    try {
      const res = await axios.post(`${API_URL}/schedule/create/${newSchedule.eventId}`, newSchedule);
      await fetchEvents(); // Reloads updated eventSchedules + sessions
      setNewSchedule({ date: "", eventId: null });
      setOpenForm({ eventId: null, type: null });
      alert("Schedule added!");
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
      await fetchEvents();
      setOpenForm({ eventId: null, type: null });
      alert("Session added!");
    } catch (err) {
      console.error("Failed to create session:", err);
    }
  };

  const handleEventUpdate = async (event) => {
    try {
      const plainHeaders = { headers: { "Content-Type": "text/plain" } };
      const jsonHeaders = { headers: { "Content-Type": "application/json" } };
  
      if (editFields.name !== event.name) {
        await axios.post(
          `${API_URL}/event/update/name/${event.id}`,
          editFields.name, // no stringify
          plainHeaders
        );
      }
  
      if (editFields.description !== event.description) {
        await axios.post(
          `${API_URL}/event/update/description/${event.id}`,
          editFields.description, // no stringify
          plainHeaders
        );
      }
  
      if (parseFloat(editFields.price) !== parseFloat(event.price)) {
        await axios.post(
          `${API_URL}/event/update/price/${event.id}`,
          editFields.price, // can be string or number
          jsonHeaders
        );
      }
  
      await fetchEvents();
      setEditingEventId(null);
      alert("Event updated successfully!");
    } catch (err) {
      console.error("Failed to update event:", err);
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
            <div key={event.id} className="border border-[#D9C2A3] p-4 rounded-lg shadow bg-white flex flex-col justify-between">
              <div className="relative">
                {/* Edit button - top right */}
                <button
                  className="absolute top-0 right-0 text-blue-500 text-sm hover:underline"
                  onClick={() => {
                    setEditingEventId(event.id);
                    setEditFields({ name: event.name, description: event.description, price: event.price });
                  }}
                >
                  Edit
                </button>

                {/* Event info */}
                <h3 className="font-bold text-lg">{event.name}</h3>
                <p className="text-sm text-gray-700">{event.description}</p>
                <p className="text-sm mt-1"><strong>Type:</strong> {event.type}</p>
                <p className="text-sm text-gray-500">Price: ${event.price}</p>
              </div>

              {editingEventId === event.id && (
                <form
                  onSubmit={(e) => {
                    e.preventDefault();
                    handleEventUpdate(event);
                  }}
                  className="mt-2 flex flex-col gap-2"
                >
                  <input
                    type="text"
                    value={editFields.name}
                    onChange={(e) => setEditFields({ ...editFields, name: e.target.value })}
                    placeholder="New name"
                    className="border border-gray-300 p-1 rounded"
                  />
                  <input
                    type="text"
                    value={editFields.description}
                    onChange={(e) => setEditFields({ ...editFields, description: e.target.value })}
                    placeholder="New description"
                    className="border border-gray-300 p-1 rounded"
                  />
                  <input
                    type="number"
                    value={editFields.price}
                    onChange={(e) => setEditFields({ ...editFields, price: e.target.value })}
                    placeholder="New price"
                    className="border border-gray-300 p-1 rounded"
                  />
                  <div className="flex gap-2">
                    <button
                      type="submit"
                      className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                    >
                      Save
                    </button>
                    <button
                      type="button"
                      className="bg-gray-400 text-white px-3 py-1 rounded hover:bg-gray-500"
                      onClick={() => setEditingEventId(null)}
                    >
                      Cancel
                    </button>
                  </div>
                </form>
              )}



              {/* Schedules and Sessions */}
              <div className="mt-2">
                {(eventSchedules[event.id] || []).map((schedule) => (
                  <div key={schedule.id} className="mb-2">
                    <p className="text-sm font-semibold text-gray-700">• Schedule: {schedule.date}</p>
                    <ul className="ml-6  text-sm text-gray-600">
                    {(eventSessions[event.id] || [])
                    .filter((sess) => sess.scheduleId === schedule.id)
                    .map((sess) => {
                      const formatTime = (timeStr) => {
                        const [hour, minute] = timeStr.split(":");
                        return new Date(0, 0, 0, hour, minute).toLocaleTimeString([], {
                          hour: "2-digit",
                          minute: "2-digit",
                          hour12: true,
                        });
                      };

                      return (
                        <li key={sess.id}>
                          - {sess.title} ({formatTime(sess.startTime)} - {formatTime(sess.endTime)}) @ {sess.location}
                        </li>
                      );
                    })}
                    </ul>
                  </div>
                ))}
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
                    className="border border-[#D9C2A3] p-2 rounded"
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
                    className="border border-[#D9C2A3] p-2 rounded"
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
                    className="border border-[#D9C2A3] p-2 rounded"
                    required
                  />
                  <input
                    type="time"
                    value={newSession.startTime}
                    onChange={(e) => setNewSession({ ...newSession, startTime: e.target.value })}
                    className="border border-[#D9C2A3] p-2 rounded"
                    required
                  />
                  <input
                    type="time"
                    value={newSession.endTime}
                    onChange={(e) => setNewSession({ ...newSession, endTime: e.target.value })}
                    className="border border-[#D9C2A3] p-2 rounded"
                    required
                  />
                  <input
                    type="text"
                    placeholder="Location"
                    value={newSession.location}
                    onChange={(e) => setNewSession({ ...newSession, location: e.target.value })}
                    className="border border-[#D9C2A3] p-2 rounded"
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

      {/* Event Creation Form in Box */}
      <div className="mt-8 w-full max-w-3xl border border-[#D9C2A3] rounded-2xl shadow p-6 bg-white">
        <h2 className="text-2xl font-bold">Event Creation</h2>
        <p className="text-gray-600 mt-2 mb-4">Create your event in a few clicks!</p>

        <form onSubmit={handleCreateEvent} className="flex flex-col gap-2">
          <input
            type="text"
            placeholder="Event Name"
            value={newEvent.name}
            onChange={(e) => setNewEvent({ ...newEvent, name: e.target.value })}
            className="border border-[#D9C2A3] p-2 rounded"
            required
          />
          <input
            type="text"
            placeholder="Description"
            value={newEvent.description}
            onChange={(e) => setNewEvent({ ...newEvent, description: e.target.value })}
            className="border border-[#D9C2A3] p-2 rounded"
            required
          />
          <select
            value={newEvent.type}
            onChange={(e) => setNewEvent({ ...newEvent, type: e.target.value })}
            className="border border-[#D9C2A3] p-2 rounded"
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
            className="border border-[#D9C2A3] p-2 rounded"
            required
          />
          <button type="submit" className="bg-[#D9C2A3] text-[#2E2E2E] p-2 rounded transition duration-300 hover:bg-[#C4A88E]">Create Event</button>
        </form>
      </div>
    </div>
  );
};

export default EventPlanning;
