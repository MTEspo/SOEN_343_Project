package backend343.chatRoom;

import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRoomRepository chatroomRepository;
    private final UserRepository userRepository;

    // User joins a chatroom
    public void joinChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        chatroom.getUsers().add(user);
        chatroom.addObserver(user);
        chatroomRepository.save(chatroom);
    }

    public void leaveChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        chatroom.getUsers().remove(user);
        chatroom.removeObserver(user);
        chatroomRepository.save(chatroom);
    }

    // Send a message
    public void sendMessage(Long chatroomId, Long userId, String content) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User sender = userRepository.findById(userId).orElseThrow();

        Message message = new Message(null, content, LocalDateTime.now(), chatroom, sender);
        chatroom.addMessage(message);
        chatroomRepository.save(chatroom);
    }
}
