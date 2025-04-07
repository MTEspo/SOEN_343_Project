
import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api";

const FinancialDashboard = () => {
  const [transactions, setTransactions] = useState([]);
  const [totalRevenue, setTotalRevenue] = useState(0);

  const [newType, setNewType] = useState("TICKET");
  const [newDescription, setNewDescription] = useState("");
  const [newAmount, setNewAmount] = useState("");

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const res = await axios.get(`${API_URL}/payments/transactions`);
      setTransactions(res.data);
      const total = res.data.reduce((sum, tx) => sum + (tx.amount || 0), 0);
      setTotalRevenue(total);
    } catch (err) {
      console.error("Failed to fetch transactions", err);
    }
  };

  const handleAddTransaction = async (e) => {
    e.preventDefault();

    const payload = {
      type: newType,
      description: newDescription,
      amount: parseFloat(newAmount),
    };

    try {
      await axios.post(`${API_URL}/payments/add`, payload);
      setNewType("TICKET");
      setNewDescription("");
      setNewAmount("");
      fetchTransactions(); 
    } catch (err) {
      console.error("Failed to add transaction", err);
    }
  };

  return (
    <div className="max-w-5xl mx-auto p-6 mt-6">
      <h2 className="text-3xl font-bold mb-6 text-center">Financial Dashboard</h2>

      {/* Total Revenue */}
      <div className="mt-8 w-full max-w-3xl border border-[#D9C2A3] rounded-2xl shadow p-6 bg-white">
        <h3 className="text-xl font-semibold mb-2">Total Revenue</h3>
        <p className="text-2xl font-bold text-green-600">${totalRevenue.toFixed(2)}</p>
      </div>

      {/* Transaction List */}
      <div className="mt-8 w-full max-w-3xl border border-[#D9C2A3] rounded-2xl shadow p-6 bg-white">
        <h3 className="text-xl font-semibold mb-4">Transactions</h3>
        <ul className="divide-y divide-gray-200">
          {transactions.map((tx) => (
            <li key={tx.id} className="py-3 flex justify-between">
              <span>
                {tx.type === "TICKET" && "🎟️ Ticket"}{" "}
                {tx.type === "DISCOUNT" && "🔻 Discount"}{" "}
                {tx.type === "SPONSOR" && "🤝 Sponsor"} - {tx.description}
              </span>
              <span
                className={`font-semibold ${
                  tx.amount < 0 ? "text-red-600" : "text-green-600"
                }`}
              >
                {tx.amount < 0 ? "-" : "+"}${Math.abs(tx.amount).toFixed(2)}
              </span>
            </li>
          ))}
        </ul>
      </div>

      {/* Add Manual Transaction */}
      <div className="mt-8 w-full max-w-3xl border border-[#D9C2A3] rounded-2xl shadow p-6 bg-white">
        <h3 className="text-xl font-semibold mb-4">Add Manual Transaction</h3>
        <form onSubmit={handleAddTransaction} className="flex flex-col gap-4">
          <select
            value={newType}
            onChange={(e) => setNewType(e.target.value)}
            className="border border-[#D9C2A3] p-2 rounded"
            required
          >
            <option value="TICKET">🎟️ Ticket</option>
            <option value="DISCOUNT">🔻 Discount</option>
            <option value="SPONSOR">🤝 Sponsor</option>
          </select>

          <input
            type="text"
            placeholder="Description"
            value={newDescription}
            onChange={(e) => setNewDescription(e.target.value)}
            className="border border-[#D9C2A3] p-2 rounded"
            required
          />

          <input
            type="number"
            step="0.01"
            placeholder="Amount"
            value={newAmount}
            onChange={(e) => setNewAmount(e.target.value)}
            className="border border-[#D9C2A3] p-2 rounded"
            required
          />

          <button
            type="submit"
            className="bg-[#D9C2A3] text-[#2E2E2E] px-4 py-2 rounded hover:bg-[#C4A88E]"
          >
            Add Transaction
          </button>
        </form>
      </div>
    </div>
  );
};

export default FinancialDashboard;
