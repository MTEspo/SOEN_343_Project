import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import ChatRoom from './ChatRoom';

const ChatRoomContainer = () => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const ws = useRef(null);
  const [joined, setJoined] = useState(false);
  const { chatroomId } = useParams();  // Get the chatroom ID from the URL
  const userId = localStorage.getItem("userId");  // Get the user ID from local storage
  const navigate = useNavigate();

  // Automatically join chatroom on page load
  useEffect(() => {
    handleJoinChatroom();
  }, [chatroomId, userId]);

  // Establish WebSocket connection
  useEffect(() => {
    if (joined && chatroomId && userId) {
      if (ws.current) {
        ws.current.close(); // Ensure we don't create multiple connections
      }

      const wsUrl = `ws://localhost:8080/chat/${chatroomId}?userId=${userId}`;
      ws.current = new WebSocket(wsUrl);
      console.log("Connected to WebSocket:", wsUrl);

      ws.current.onmessage = (event) => {
        console.log(`Received message: ${event.data}`);
        setMessages((prevMessages) => [...prevMessages, event.data]);
      };

      ws.current.onerror = (event) => {
        console.error('WebSocket error:', event);
      };

      ws.current.onclose = () => {
        console.log('WebSocket disconnected');
      };

      return () => {
        if (ws.current) {
          ws.current.close();
        }
      };
    }
  }, [joined, chatroomId, userId]);

  const handleJoinChatroom = () => {
    if (!joined && chatroomId && userId) {
      console.log("Attempting to join chatroom...");
      axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/join/${userId}`)
        .then((response) => {
          console.log("Join response:", response.data);
          setJoined(true);
          setMessages([]);
        })
        .catch((error) => console.error("Error joining chatroom:", error));
    }
  };

  // Leave chatroom and navigate back
  const handleLeaveChatroom = () => {
    axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/leave/${userId}`)
      .then((response) => {
        console.log(response.data.numberUsersInChatRoom);
        setJoined(false);
        if (ws.current) {
          ws.current.close();
          ws.current = null;
        }
        navigate('/sessions');
      })
      .catch((error) => console.error(error));
  };

  // Send a message through the WebSocket
  const handleSendMessage = () => {
    const username = localStorage.getItem("username");
    if (ws.current && ws.current.readyState === WebSocket.OPEN) {
      const messageData = JSON.stringify({ 
        userId, 
        message: `${username}: ${newMessage}`
      });
      ws.current.send(messageData);
      setNewMessage('');
    } else {
      console.error('WebSocket is not connected');
    }
  };

  return (
    <div>
      <ChatRoom
        messages={messages}
        newMessage={newMessage}
        handleSendMessage={handleSendMessage}
        setNewMessage={setNewMessage}
        joined={joined}
        handleLeaveChatroom={handleLeaveChatroom}
      />
    </div>
  );
};

export default ChatRoomContainer;
