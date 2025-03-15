package backend343.chatRoom;

import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.repository.UserRepository;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRoomRepository chatroomRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final ChatRoomHandler chatRoomHandler;

    // User joins a chatroom
    public void joinChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().add(user);
        chatroom.addObserver(user);
        chatroomRepository.save(chatroom);
    }

    public void leaveChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().remove(user);
        chatroom.removeObserver(user);
        chatroomRepository.save(chatroom);
    }

    public void sendMessage(Long chatroomId, Long userId, String content) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User sender = userDetailsService.getUserById(userId);

        Message message = Message.builder()
                .content(content)
                .timestamp(LocalDateTime.now())
                .chatroom(chatroom)
                .sender(sender)
                .build();

        chatroom.addMessage(message);
        chatroomRepository.save(chatroom);
        chatRoomHandler.broadcastMessage("New message in chat room " + chatroomId);
    }
}
