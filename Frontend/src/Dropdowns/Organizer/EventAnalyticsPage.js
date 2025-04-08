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
        console.log(eventId)
        const res = await axios.get(`${API_URL}event/${eventId}/get-event-analytics`);
        setAnalytics(res.data);
      } catch (err) {
        console.error("Failed to fetch event analytics:", err);
      }
    };
    fetchAnalytics();
  }, [eventId]);

  if (!analytics) return <div>Loading...</div>;

  return (
    <div>
      <h1>Event Analytics</h1>
      <p>Total Amount Generated: ${analytics.amountGenerated}</p>
      <p>Number of Speakers: {analytics.speakers.length}</p>
      <p>Number of Attendees: {analytics.attendees.length}</p>
      <h2>Speakers:</h2>
      <ul style={{
        listStyle: 'none',
        padding: 0,
        margin: 0
      }}>
        {analytics.speakers.map((speaker) => (
          <li key={speaker.id} style={{
            padding: '10px',
            borderBottom: '1px solid #ccc'
          }}>
            <strong>Speaker Email:</strong> {speaker.email}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default EventsAnalyticsPage;