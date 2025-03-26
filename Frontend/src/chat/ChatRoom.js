import React from 'react';

const ChatRoom = ({
  messages,
  newMessage,
  handleSendMessage,
  setNewMessage,
  handleLeaveChatroom,
  joined,
}) => {
  return (
    <div className="p-4">
      <h1 className="text-3xl font-bold mb-4">Chat Room</h1>
      {joined ? (
        <div>
          <div className="mb-4 p-2 bg-gray-100 rounded-md h-60 overflow-y-scroll">
            {messages.map((message, index) => (
              <p key={index} className="p-1 bg-gray-200 my-1 rounded">{message}</p>
            ))}
          </div>
          <div className="flex space-x-2">
            <input
              type="text"
              value={newMessage}
              onChange={(event) => setNewMessage(event.target.value)}
              placeholder="Type a message..."
              className="border p-2 rounded w-full"
            />
            <button
              onClick={handleSendMessage}
              disabled={!newMessage.trim()}
              className="bg-blue-500 text-white p-2 rounded hover:bg-blue-600"
            >
              Send
            </button>
          </div>
          <button
            onClick={handleLeaveChatroom}
            className="mt-4 bg-red-500 text-white p-2 rounded hover:bg-red-600"
          >
            Leave Chat Room
          </button>
        </div>
      ) : (
        <p>Please join a chatroom first.</p>
      )}
    </div>
  );
};

export default ChatRoom;
