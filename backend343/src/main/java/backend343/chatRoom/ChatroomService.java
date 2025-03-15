package backend343.chatRoom;

import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRoomRepository chatroomRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public void joinChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().add(user);
        chatroom.addObserver(user);
        chatroomRepository.save(chatroom);
    }

    public int leaveChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().remove(user);
        chatroom.removeObserver(user);
        chatroomRepository.save(chatroom);
        return chatroom.getUsers().size();

    }

    @Transactional
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
    }

    public List<Message> getExistingMessages(Long chatroomId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        return chatroom.getMessages();
    }
}
