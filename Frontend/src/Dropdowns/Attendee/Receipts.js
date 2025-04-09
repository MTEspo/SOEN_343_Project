import React, { useState, useEffect } from "react";
import axios from "axios";
const API_URL = "http://localhost:8080/api/";

const Receipts = () => {
  const [tickets, setTickets] = useState([]);
  const attendeeId = localStorage.getItem("userId");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTickets = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`${API_URL}tickets/all-users-tickets/${attendeeId}`);
        setTickets(response.data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };
    fetchTickets();
  }, [attendeeId]);

  const handleRefund = async (ticket) => {
    try {
      const refundData = {
        sessionId: ticket.session.id,
        userId: attendeeId,
      };
      await axios.post(`${API_URL}refund/v1/refund`, refundData);
      // Update the ticket status to REFUNDED
      setTickets(tickets.map((t) => {
        if (t.id === ticket.id) {
          return { ...t, status: 'REFUNDED' };
        }
        return t;
      }));
    } catch (error) {
      console.error(error);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col min-h-screen justify-center items-center">
        <p className="text-lg">Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col min-h-screen justify-center items-center">
        <p className="text-lg text-red-500">Error: {error}</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col min-h-screen">
      {/* Main Content */}
      <div className="flex-grow flex flex-col p-8">
        <h1 className="text-4xl font-bold text-center text-[#2E2E2E] mb-4">
          Receipts
        </h1>
        <p className="text-center text-[#5A5958] mb-10">
          View your ticket receipts.
        </p>

        {tickets.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
            {tickets.map((ticket) => (
              <div
                key={ticket.id}
                className="bg-white border border-[#D9C2A3] p-4 rounded-lg shadow h-auto"
              >
                <h2 className="text-xl font-bold mb-1">Ticket Code: {ticket.ticketCode}</h2>
                <p className="text-sm text-gray-600">{ticket.session.title}</p>
                <div className="mt-3">
                  <p className="text-sm">
                    <strong>Registration Date:</strong> {new Date(ticket.registrationDate).toLocaleString()}
                  </p>
                  <p className="text-sm">
                    <strong>Amount Paid:</strong> ${ticket.amountPaid.toFixed(2)}
                  </p>
                  <p className="text-sm">
                    <strong>Status:</strong> {ticket.status}
                  </p>
                </div>
                {ticket.status === 'ACTIVE' && (
                  <button
                    onClick={() => handleRefund(ticket)}
                    className="mt-4 w-full bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded transition duration-300 hover:bg-[#C4A88E]"
                  >
                    Refund
                  </button>
                )}
              </div>
            ))}
          </div>
        ) : (
          <div className="mt-8 text-center text-lg">
            <p>No tickets found.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Receipts;