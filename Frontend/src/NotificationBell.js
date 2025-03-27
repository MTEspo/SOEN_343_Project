import React, { useState, useEffect } from "react";
import axios from "axios";
import { AiOutlineBell } from "react-icons/ai";

const NotificationBell = () => {
  const [notificationCount, setNotificationCount] = useState(0);
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchNotifications = async () => {
      if (userId) {
        try {
          const response = await axios.get(`http://localhost:8080/api/users/${userId}/notifications`);
          setNotificationCount(response.data);
        } catch (error) {
          console.error("Error fetching notifications:", error);
        }
      }
    };

    fetchNotifications();
  }, [userId]);

  return (
    <div className="flex items-center space-x-1">
      <AiOutlineBell size={24} className="text-[#5A5958]" />
      <span className="text-lg text-[#5A5958]">{notificationCount}</span>
    </div>
  );
};

export default NotificationBell;
