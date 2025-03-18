import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api/event";

const EventPlanning = () => {
  const [events, setEvents] = useState([]); // Store events
  const [newEvent, setNewEvent] = useState({ name: "", description: "", price: "", type: "" }); // Default empty type

  // Fetch events on page load
  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const response = await axios.get(`${API_URL}/all`);
      setEvents(response.data);
    } catch (error) {
      console.error("Error fetching events:", error);
    }
  };

  // Create a new event
  const handleCreateEvent = async (e) => {
    e.preventDefault();

    if (newEvent.type === "" || newEvent.type === "SELECT TYPE") {
      alert("Please select a valid event type before submitting.");
      return;
    }

    try {
      const response = await axios.post(`${API_URL}/create`, newEvent);
      setEvents([...events, response.data]); // Update UI with new event
      setNewEvent({ name: "", description: "", price: "", type: "" }); // Reset form
    } catch (error) {
      console.error("Error creating event:", error);
    }
  };

  return (
    <div className="flex flex-col items-center mt-6">
      <h2 className="text-2xl font-bold">Event Planning</h2>
      <p className="text-gray-600 mt-2">Manage your events efficiently.</p>

      {/* Event Creation Form */}
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
          placeholder="Event Description"
          value={newEvent.description}
          onChange={(e) => setNewEvent({ ...newEvent, description: e.target.value })}
          className="border p-2 rounded"
          required
        />
        
        {/* Event Type Dropdown */}
        <select
          value={newEvent.type}
          onChange={(e) => setNewEvent({ ...newEvent, type: e.target.value })}
          className={`border p-2 rounded ${newEvent.type === "" ? "text-gray-400" : "text-black"}`} // Grey default text
          required
        >
          <option value="">Event type</option>
          <option value="SEMINAR" className="text-black">SEMINAR</option>
          <option value="WEBINAR" className="text-black">WEBINAR</option>
          <option value="WORKSHOP" className="text-black">WORKSHOP</option>
          <option value="CONFERENCE" className="text-black">CONFERENCE</option>
        </select>

        <input
          type="number"
          placeholder="Price"
          value={newEvent.price}
          onChange={(e) => setNewEvent({ ...newEvent, price: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <button type="submit" className="bg-blue-500 text-white p-2 rounded">Create Event</button>
      </form>

      {/* Display Events */}
      <ul className="mt-4">
        {events.length === 0 ? (
          <p>No events available.</p>
        ) : (
          events.map((event) => (
            <li key={event.id} className="border p-2 my-2 rounded shadow">
              <h3 className="font-semibold">{event.name}</h3>
              <p>{event.description}</p>
              <p><strong>Type:</strong> {event.type}</p>
              <p className="text-sm text-gray-500">Price: ${event.price}</p>
            </li>
          ))
        )}
      </ul>
    </div>
  );
};

export default EventPlanning;
