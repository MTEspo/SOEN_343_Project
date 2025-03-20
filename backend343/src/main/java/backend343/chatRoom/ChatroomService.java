package backend343.chatRoom;

import backend343.logger.LoggerSingleton;
import backend343.models.Session;
import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.repository.SessionRepository;
import backend343.service.TicketService;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRoomRepository chatroomRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TicketService ticketService;
    private final SessionRepository sessionRepository;

    public void joinChatroom(Long chatroomId, Long userId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        User user = userDetailsService.getUserById(userId);

        chatroom.getUsers().add(user);
        chatroom.addObserver(user);
        chatroomRepository.save(chatroom);

        //reset notifications for user associated with that chatroom
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

    public void notifyObservers(Long chatroomId, Long senderId) {
        // Retrieve the session associated with the chatroom
        Optional<Session> optionalSession = sessionRepository.findById(chatroomId);

        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            List<User> usersWithAccess = ticketService.getUsersWithAccessToSession(session.getId());

            for (User user : usersWithAccess) {
                //check if the user is currently in the chatroom
                boolean isUserInChatroom = chatroomRepository.isUserInChatroom(chatroomId, user.getId());

                if (!isUserInChatroom && !user.getId().equals(senderId)) {
                    user.update(chatroomId); //add a notif if theyre not in room right now
                }
            }
        } else {
            LoggerSingleton.getInstance().logError("Session not found for chatroom ID: " + chatroomId);
        }
    }
}
