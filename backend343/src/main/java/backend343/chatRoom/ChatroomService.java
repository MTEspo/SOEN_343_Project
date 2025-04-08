package backend343.chatRoom;

import backend343.logger.LoggerSingleton;
import backend343.models.Speaker;
import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.service.SpeakerService;
import backend343.service.TicketService;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRoomRepository chatroomRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TicketService ticketService;
    private final SpeakerService speakerService;

    public void joinChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().add(user);
        chatroom.addObserver(user);
        chatroomRepository.save(chatroom);

        user.resetChatroomNotifications(chatroomId);
        userDetailsService.saveUser(user);
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
        //Get all speakers who were assigned to this chatroom
        List<Speaker> speakers = speakerService.getSpeakersByChatroomId(chatroomId);
        // Get users currently in the chatroom
        List<User> usersInChatroom = getUsersInChatroom(chatroomId);

        Set<Long> notifiedIds = new HashSet<>();
    
        for (User user : usersWithAccess) {
            notifiedIds.add(user.getId());
            if (!usersInChatroom.contains(user) && !user.getId().equals(senderId)) {
                user.incrementChatroomNotifications(chatroomId);
                LoggerSingleton.getInstance().logInfo("Notification incremented for ticket user: " + user.getId());
            }
        }
    
        for (Speaker speaker : speakers) {
            if (!notifiedIds.contains(speaker.getId()) && !usersInChatroom.contains(speaker) && !speaker.getId().equals(senderId)) {
                speaker.incrementChatroomNotifications(chatroomId);
                LoggerSingleton.getInstance().logInfo("Notification incremented for speaker: " + speaker.getId());
            }
        }
    
        LoggerSingleton.getInstance().logInfo("Notification process completed for chatroom ID: " + chatroomId);
    }
}
