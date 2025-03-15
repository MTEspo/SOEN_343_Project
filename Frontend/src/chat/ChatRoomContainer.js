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

  useEffect(() => {
    const handleBeforeUnload = () => {
      axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/leave/${userId}`)
        .then((response) => {
          console.log(response.data);
        })
        .catch((error) => {
          console.error(error);
        });
    };
  
    window.addEventListener('beforeunload', handleBeforeUnload);
  
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [chatroomId, userId]);

  useEffect(() => {
    if (joined) {
      if (!ws.current || ws.current.readyState === WebSocket.CLOSED) {
        ws.current = new WebSocket(`ws://localhost:8080/chat/${chatroomId}`);
  
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
      }
    }
  }, [joined, chatroomId]);
  
  useEffect(() => {
    return () => {
      if (ws.current && ws.current.readyState === WebSocket.OPEN) {
        ws.current.close();
      }
    };
  }, []);

  const handleJoinChatroom = () => {
    axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/join/${userId}`)
      .then((response) => {
        console.log(response.data);
        setJoined(true);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handleLeaveChatroom = () => {
    axios.post(`http://localhost:8080/api/chatrooms/${chatroomId}/leave/${userId}`)
      .then((response) => {
        console.log(response.data.numberUsersInChatRoom);
        if (response.data.numberUsersInChatRoom === 0) {
          if (ws.current && ws.current.readyState === WebSocket.OPEN) {
            console.log("here")
            ws.current.close();
          }
        }
        setJoined(false);
        setMessages([]); // Reset the messages state variable
      })
      .catch((error) => {
        console.error(error);
      });
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
      <button onClick={handleLeaveChatroom}>Leave Chat Room</button>
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