package backend343.chatRoom;

import backend343.logger.LoggerSingleton;
import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.service.TicketService;
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
    private final TicketService ticketService;

    public void joinChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().add(user);
        chatroom.addObserver(user);
        chatroomRepository.save(chatroom);

        user.resetChatroomNotifications(chatroomId);
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

    public List<User> getUsersInChatroom(Long chatroomId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new RuntimeException("Chatroom not found"));
        return chatroom.getUsers();
    }

    public void notifyObservers(Long chatroomId, Long senderId) {
        LoggerSingleton.getInstance().logInfo("Starting notification for chatroom ID: " + chatroomId + " and sender ID: " + senderId);
    
        // Get all users who have access to this chatroom
        List<User> usersWithAccess = ticketService.getUsersBySessionId(chatroomId);    
        // Get users currently in the chatroom
        List<User> usersInChatroom = getUsersInChatroom(chatroomId);
    
        for (User user : usersWithAccess) {
            // Notify users who are not in the chatroom and are not the sender
            if (!usersInChatroom.contains(user) && !user.getId().equals(senderId)) {
                user.incrementChatroomNotifications(chatroomId);
                LoggerSingleton.getInstance().logInfo("Notification incremented for user: " + user.getId() + " for chatroom: " + chatroomId);
            } else {
                LoggerSingleton.getInstance().logInfo("No notification for user: " + user.getId() + " - Either in chatroom or sender.");
            }
        }
        LoggerSingleton.getInstance().logInfo("Notification process completed for chatroom ID: " + chatroomId);
    }
}
