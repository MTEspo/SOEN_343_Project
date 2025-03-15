import React from 'react';

const ChatRoom = ({
  messages,
  newMessage,
  handleSendMessage,
  setNewMessage,
  joined,
  handleJoinChatroom,
  handleLeaveChatroom,
}) => {
  return (
    <div>
      <h1>Chat Room</h1>
      {joined ? (
        <div>
          <div>
            {messages.map((message, index) => (
              <p key={index}>{message}</p>
            ))}
          </div>
          <input
            type="text"
            value={newMessage}
            onChange={(event) => setNewMessage(event.target.value)}
            placeholder="Type a message..."
          />
          <button onClick={handleSendMessage}>Send</button>
          <button onClick={handleLeaveChatroom}>Leave Chatroom</button>
        </div>
      ) : (
        <div>
          <button onClick={handleJoinChatroom}>Join Chatroom</button>
        </div>
      )}
    </div>
  );
};

export default ChatRoom;