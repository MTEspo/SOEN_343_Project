import React from 'react';

const ChatRoom = ({
  messages,
  newMessage,
  handleSendMessage,
  setNewMessage,
  joined,
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
          <button onClick={handleSendMessage} disabled={!newMessage.trim()}>Send</button>
        </div>
      ) : (
        <p>Please join a chatroom first.</p>
      )}
    </div>
  );
};

export default ChatRoom;
