import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';



const API_URL = "http://localhost:8080/api";

const EventPlanning = () => {
  const [events, setEvents] = useState([]);
  const [schedules, setSchedules] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [eventSchedules, setEventSchedules] = useState({});
  const [eventSessions, setEventSessions] = useState({});
  const [editingEventId, setEditingEventId] = useState(null);
  const [editFields, setEditFields] = useState({ name: "", description: "", price: "" });
  const navigate = useNavigate();
  const [savedResources, setSavedResources] = useState({});

  const [newEvent, setNewEvent] = useState({ name: "", description: "", price: "", type: "" });
  const [newSchedule, setNewSchedule] = useState({ date: "", eventId: null });
  const [newSession, setNewSession] = useState({ title: "", startTime: "", endTime: "", location: "", scheduleId: "" });
  const [availableTags, setAvailableTags] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);


  const [openForm, setOpenForm] = useState({ eventId: null, type: null });

  const token = localStorage.getItem("token");
  const organizerId = localStorage.getItem("userId");

  const [showSpeakerInfo, setShowSpeakerInfo] = useState(false); 

  const [selectedFiles, setSelectedFiles] = useState({});
  const [dragging, setDragging] = useState({});


  const handleDrop = (eventId, e) => {
    e.preventDefault();
    const files = e.dataTransfer.files;
    setSelectedFiles((prevFiles) => ({ ...prevFiles, [eventId]: [...(prevFiles[eventId] || []), ...files] }));
    setDragging((prevDragging) => ({ ...prevDragging, [eventId]: false }));
  };

  const handleDragOver = (eventId, e) => {
    e.preventDefault();
    setDragging((prevDragging) => ({ ...prevDragging, [eventId]: true }));
  };

  const handleDragLeave = (eventId, e) => {
    e.preventDefault();
    setDragging((prevDragging) => ({ ...prevDragging, [eventId]: false }));
  };

  const handleFileChange = (eventId, e) => {
    const files = e.target.files;
    setSelectedFiles((prevFiles) => ({ ...prevFiles, [eventId]: [...(prevFiles[eventId] || []), ...files] }));
  };

  const handleRemoveFile = async (eventId, fileId) => {
    try {
      await axios.post(`${API_URL}/event/${eventId}/remove-file/${fileId}`);
      await fetchLatestResources(eventId);
    } catch (error) {
      console.error(error);
    }
  };

  const handleSaveFiles = async (eventId) => {
    if (!selectedFiles[eventId] || selectedFiles[eventId].length === 0) return;

    const formData = new FormData();
    selectedFiles[eventId].forEach((file) => {
      formData.append("files", file);
    });

    try {
      await axios.post(`${API_URL}/event/${eventId}/add-files`, formData);
      setSelectedFiles((prevFiles) => ({ ...prevFiles, [eventId]: [] }));
      await fetchLatestResources(eventId);
      alert("Files saved successfully!");
    } catch (err) {
      console.error("Failed to save files:", err);
    }
  };

  useEffect(() => {
    fetchEvents();
    fetchAllSchedules();
    fetchAllSessions();
    fetchAvailableTags();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await axios.get(`${API_URL}/organizer/${organizerId}/events`);
      setEvents(res.data);
      await loadSchedulesAndSessionsForEvents(res.data);
      
    } catch (err) {
    console.error("Failed to fetch events:", err);
    }
  };

  const fetchAllSchedules = async () => {
    try {
      const res = await axios.get(`${API_URL}/schedule/all-schedules`);
      setSchedules(res.data);
    } catch (err) {
      console.error("Failed to fetch all schedules:", err);
    }
  };

  const fetchAllSessions = async () => {
    try {
      const res = await axios.get(`${API_URL}/session/all-sessions`);
      setSessions(res.data);
    } catch (err) {
      console.error("Failed to fetch all sessions:", err);
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

  const fetchLatestResources = async (eventId) => {
    try {
      const response = await axios.get(`${API_URL}/event/${eventId}/resources`);
      setSavedResources((prevResources) => ({ ...prevResources, [eventId]: response.data }));
    } catch (error) {
      console.error(error);
    }
  };

  const fetchAvailableTags = async () => {
    try {
      const res = await axios.get(`${API_URL}/tags/all-tags`);
      setAvailableTags(res.data);
    } catch (err) {
      console.error("Failed to fetch tags:", err);
    }
  };  

  const loadSchedulesAndSessionsForEvents = async (eventList) => {
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
        if (!eventScheduleMap[eventId]) eventScheduleMap[eventId] = [];
        eventScheduleMap[eventId].push(schedule);
      }

      for (const session of allSessions) {
        const scheduleRes = await axios.get(`${API_URL}/session/${session.id}/schedule`);
        const scheduleId = scheduleRes.data;

        const eventId = Object.keys(eventScheduleMap).find(eid =>
          eventScheduleMap[eid].some(s => s.id === scheduleId)
        );

        if (!eventId) continue;

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

    console.log("Organizer ID:", organizerId);
    console.log("New Event:", newEvent);

    if (!newEvent.type || !organizerId) return;

    try {
      console.log("Organizer ID:", organizerId);
      console.log("New Event:", newEvent);


      const res = await axios.post(
        `${API_URL}/event/create/${organizerId}`,
        { ...newEvent, tags: selectedTags},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      setEvents([...events, res.data]);
      setNewEvent({ name: "", description: "", price: "", type: "" });
      alert("Event created successfully!");
    } catch (err) {
      console.error("Failed to create event:", err);
    }
  };

  const handleTagToggle = (tag) => {
    if (selectedTags.includes(tag)) {
      setSelectedTags(selectedTags.filter((t) => t !== tag));
    } else if (selectedTags.length < 3) {
      setSelectedTags([...selectedTags, tag]);
    }
  };  

  const handleCreateSchedule = async (e) => {
    e.preventDefault();
    if (!newSchedule.date || !newSchedule.eventId) return;

    try {
      await axios.post(`${API_URL}/schedule/create/${newSchedule.eventId}`, newSchedule);
      await fetchEvents();
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
      await axios.post(`${API_URL}/session/create/${newSession.scheduleId}`, newSession);
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
        await axios.post(`${API_URL}/event/update/name/${event.id}`, editFields.name, plainHeaders);
      }
      if (editFields.description !== event.description) {
        await axios.post(`${API_URL}/event/update/description/${event.id}`, editFields.description, plainHeaders);
      }
      if (parseFloat(editFields.price) !== parseFloat(event.price)) {
        await axios.post(`${API_URL}/event/update/price/${event.id}`, editFields.price, jsonHeaders);
      }

      await fetchEvents();
      setEditingEventId(null);
      alert("Event updated successfully!");
    } catch (err) {
      console.error("Failed to update event:", err);
    }
  };
    
  return (
    <div className="min-h-screen flex flex-col items-center mt-6">
      <h2 className="text-2xl font-bold pt-6">Plan Events</h2>
      <p className="text-gray-600 mt-2">Plan and Manage your events efficiently.</p>

      {/* Display Events in Grid Format */}
      {events.length > 0 && (
        <div className="mt-8 w-full max-w-7xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
        {events.map((event) => (
            <div
              key={event.id}
              className="border border-[#D9C2A3] p-6 rounded-lg shadow bg-white flex flex-col justify-between h-full"
            >
              <div className="relative">
                {/* Edit button - top right */}
                <button
                  className="absolute top-0 right-0 text-[#8B5E3C] text-sm hover:underline"
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


              <div className="mt-3 space-y-4">
                {/* Button Row */}
                <div className="flex flex-wrap gap-2">
                  {/* ADD SCHEDULE BUTTON*/}
                  <button
                    className="w-full text-center bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded-md transition duration-300 hover:bg-[#C4A88E]"
                    onClick={() => {
                      setOpenForm({ eventId: event.id, type: 'schedule' });
                      setNewSchedule({ date: "", eventId: event.id });
                    }}
                  >
                    Add Schedule
                  </button>

                  {/* ADD SESSION BUTTON*/}
                  <button
                    className="w-full text-center bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded-md transition duration-300 hover:bg-[#C4A88E]"
                    onClick={async () => {
                      await fetchSchedulesForEvent(event.id);
                      setOpenForm({ eventId: event.id, type: 'session' });
                    }}
                  >
                    Add Session
                  </button>

                  {/* CONTACT SPEAKERS BUTTON */}
                  <button
                    className="w-full text-center bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded-md transition duration-300 hover:bg-[#C4A88E]"
                    onClick={() => navigate(`/contact-speakers/${event.id}`)}
                  >
                    Contact Speakers
                  </button>
                </div>

                {/* File Upload Section */}
                <div>
                  <h3 className="font-bold text-lg">Upload Files</h3>
                  <div
                    className={`border-2 border-dashed p-4 rounded-lg ${
                      dragging[event.id] ? "border-green-500" : "border-gray-300"
                    }`}
                    onDragOver={(e) => handleDragOver(event.id, e)}
                    onDragLeave={(e) => handleDragLeave(event.id, e)}
                    onDrop={(e) => handleDrop(event.id, e)}
                  >
                    {selectedFiles[event.id] && selectedFiles[event.id].length > 0 ? (
                      <ul>
                        {selectedFiles[event.id].map((file, index) => (
                          <li key={index}>{file.name}</li>
                        ))}
                      </ul>
                    ) : (
                      <p>Drag and drop files or click to upload</p>
                    )}
                    <input
                      type="file"
                      multiple
                      onChange={(e) => handleFileChange(event.id, e)}
                      className="hidden"
                      id={`file-input-${event.id}`}
                    />
                    <label
                      htmlFor={`file-input-${event.id}`}
                      className="flex justify-center bg-[#D9C2A3] text-[#2E2E2E] px-3 py-1 rounded transition duration-300 hover:bg-[#C4A88E]"
                    >
                      Browse Files
                    </label>
                  </div>
                  <button
                    className="mt-2 bg-[#D9C2A3] text-[#2E2E2E] px-3 py-1 rounded transition duration-300 hover:bg-[#C4A88E]"
                    onClick={() => handleSaveFiles(event.id)}
                  >
                    Save Files
                  </button>
                </div>

                  {/* Saved Resources Section */}
                <div>
                  <h3 className="font-bold text-lg">Saved Resources</h3>
                  {savedResources[event.id] && savedResources[event.id].length > 0 ? (
                    <ul className="space-y-1">
                      {savedResources[event.id].map((resource) => (
                        <li key={resource.id} className="flex items-center space-x-2">
                          <span>{resource.name}</span>
                          <button
                            className="text-white bg-red-600 border border-red-700 px-2 py-0.5 text-xs rounded hover:bg-red-700"
                            onClick={() => handleRemoveFile(event.id, resource.id)}
                          >
                            Remove
                          </button>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p>No resources for this event</p>
                  )}
                </div>
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
      <hr className="my-6 w-full max-w-6xl border-t border-gray-300" />

      {/* Event Creation Form in Box */}
      <div className="mt-6 mb-4 w-full max-w-3xl border border-[#D9C2A3] rounded-2xl shadow p-6 bg-white">
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

          {/* Tag Selection Section */}
          <label className="font-medium">Tags (select up to 3):</label>
          <div className="flex flex-wrap gap-2">
            {availableTags.map((tag) => (
              <button
                key={tag}
                type="button"
                onClick={() => handleTagToggle(tag)}
                className={`px-2 py-1 border border-[#C4A88E] rounded-md ${
                  selectedTags.includes(tag) ? 'bg-[#C4A88E] text-white' : 'bg-white text-[#2E2E2E]'
                }`}
              >
                {tag}
              </button>
            ))}
          </div>
          <p className="text-sm text-[#5A5958]">You may select up to 3 tags</p>

          <button type="submit" className="bg-[#D9C2A3] text-[#2E2E2E] p-2 rounded transition duration-300 hover:bg-[#C4A88E]">Create Event</button>
        </form>
      </div>
    </div>
  );
};

export default EventPlanning;
