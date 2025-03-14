package backend343.service;

import backend343.models.ChatRoom;
import backend343.models.User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatRoomService {
    
    private final Map<Long, ChatRoom> chatRooms = new HashMap<>(); // Maps sessionId to ChatRoom

    //chatrooms are associated to sessions
    public void createChatRoom(Long sessionId) {
        if (!chatRooms.containsKey(sessionId)) {
            chatRooms.put(sessionId, new ChatRoom());
        }
    }

    public ChatRoom getChatRoom(Long sessionId) {
        return chatRooms.get(sessionId);
    }

    //users joining a chatroom
    public void joinChat(Long sessionId, User user) {
        ChatRoom chatRoom = getChatRoom(sessionId);
        //if its a valid chatroom then that user is added as an observer of it
        if (chatRoom != null) {
            chatRoom.addObserver(user);
        }
    }

    //users leaving a chatroom
    public void leaveChat(Long sessionId, User user) {
        ChatRoom chatRoom = getChatRoom(sessionId);
        if (chatRoom != null) {
            chatRoom.removeObserver(user);
        }
    }

    //users sending a message in the chatroom
    public void sendMessage(Long sessionId, User user, String message) {
        ChatRoom chatRoom = getChatRoom(sessionId);
        if (chatRoom != null) {
            String formattedMessage = user.getUsername() + ": " + message;
            chatRoom.sendMessage(formattedMessage);
        }
    }
}
