import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

const DropdownMenu = ({ isLoggedIn, handleLogout }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [role, setRole] = useState("");

  useEffect(() => {
    const storedRole = localStorage.getItem("role");
    if (storedRole) {
      setRole(storedRole);
    }
  }, []);

  const getMenuOptions = () => {
    switch (role) {
      case "ATTENDEE":
        return [
          { label: "Your Sessions", path: "/sessions"},
          { label: "Receipts", path: "/receipts" },
        ]; // No options for now
      case "ORGANIZER":
        return [
          { label: "Promotions", path: "promotion"},
          { label: "Analytics", path: "/dropdowns/organizer/event-analytics" },
          ];
      case "SPEAKER":
        return [
          { label: "Your Offers", path: "/dropdowns/speaker/speaker-offers" },
          { label: "Sessions Your Speaking at", path: "/dropdowns/speaker/speaker-sessions" },
        ];
      default:
        return [];
    }
  };

  return (
    <div className="relative">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="w-[90px] bg-[#D9C2A3] text-[#2E2E2E] px-4 py-[6px] rounded-md text-lg leading-none font-semibold transition duration-300 hover:bg-[#C4A88E]"
      >
        Menu
      </button>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-48 bg-white shadow-lg rounded-md py-2">
          {getMenuOptions().map((item, index) => (
            <Link
              key={index}
              to={item.path}
              className="block px-4 py-2 text-gray-700 hover:bg-gray-200"
            >
              {item.label}
            </Link>
          ))}
          {isLoggedIn && (
            <button
              onClick={handleLogout}
              className="w-full text-left px-4 py-2 text-red-600 hover:bg-gray-200"
            >
              Logout
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default DropdownMenu;
