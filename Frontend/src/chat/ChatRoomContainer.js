import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import ChatRoom from './ChatRoom';

const ChatRoomContainer = () => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const ws = useRef(null); 
  const [joined, setJoined] = useState(false);
  const [chatroomId, setChatroomId] = useState('');
  const [userId, setUserId] = useState('');

  // Handle closing WebSocket when user leaves page
  useEffect(() => {
    const handleBeforeUnload = () => {
      if (chatroomId && userId) {
        axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/leave/${userId}`)
          .catch((error) => console.error(error));
      }
      if (ws.current) {
        ws.current.close();
      }
    };
  
    window.addEventListener('beforeunload', handleBeforeUnload);
  
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [chatroomId, userId]);

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
    if (!chatroomId || !userId) {
      alert("Chatroom ID and User ID are required!");
      return;
    }

    axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/join/${userId}`)
      .then((response) => {
        console.log(response.data);
        setJoined(true);
        setMessages([]); // Clear messages when rejoining
      })
      .catch((error) => console.error(error));
  };

  const handleLeaveChatroom = () => {
    axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/leave/${userId}`)
      .then((response) => {
        console.log(response.data.numberUsersInChatRoom);
        setJoined(false);
        setMessages([]); // Reset chat messages
        if (ws.current) {
          ws.current.close();
          ws.current = null;
        }
      })
      .catch((error) => console.error(error));
  };

  const handleSendMessage = () => {
    if (ws.current && ws.current.readyState === WebSocket.OPEN) {
      const messageData = JSON.stringify({ userId, message: newMessage });
      ws.current.send(messageData);
      setNewMessage('');
    } else {
      console.error('WebSocket is not connected');
    }
  };

  return (
    <div>
      <input
        type="text"
        value={chatroomId}
        onChange={(event) => setChatroomId(event.target.value)}
        placeholder="Chat Room ID"
      />
      <input
        type="text"
        value={userId}
        onChange={(event) => setUserId(event.target.value)}
        placeholder="User ID"
      />
      <button onClick={handleJoinChatroom}>Join Chat Room</button>
      <button onClick={handleLeaveChatroom} disabled={!joined}>Leave Chat Room</button>
      
      <ChatRoom
        messages={messages}
        newMessage={newMessage}
        handleSendMessage={handleSendMessage}
        setNewMessage={setNewMessage}
        joined={joined}
      />
    </div>
  );
};

export default ChatRoomContainer;
