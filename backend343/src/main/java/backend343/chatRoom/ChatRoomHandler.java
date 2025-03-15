package backend343.chatRoom;

import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.JSONObject;
import backend343.logger.LoggerSingleton;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Service
@RequiredArgsConstructor
public class ChatRoomHandler extends TextWebSocketHandler {

    private final ChatroomService chatroomService;
    private static final LoggerSingleton logger = LoggerSingleton.getInstance();
    private static final Map<Long, Set<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String[] parts = session.getUri().getPath().split("/");
        Long chatroomId = Long.parseLong(parts[parts.length - 1]);
        chatRoomSessions.computeIfAbsent(chatroomId, k -> new CopyOnWriteArraySet<>()).add(session);
        logger.logInfo("WebSocket connection established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String[] parts = session.getUri().getPath().split("/");
        Long chatroomId = Long.parseLong(parts[parts.length - 1]);
        Set<WebSocketSession> sessions = chatRoomSessions.get(chatroomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                chatRoomSessions.remove(chatroomId);
            }
        }
        logger.logInfo("WebSocket connection closed");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.logInfo("Received message: " + message.getPayload());
        String[] parts = session.getUri().getPath().split("/");
        Long chatroomId = Long.parseLong(parts[parts.length - 1]);
        String content = message.getPayload();

        if (!content.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                Long userId = jsonObject.getLong("userId");

                if (jsonObject.has("message")) {
                    String messageContent = jsonObject.getString("message");
                    chatroomService.sendMessage(chatroomId, userId, messageContent);
                    broadcastMessage(chatroomId, messageContent);
                } else {
                    logger.logInfo("User joined chat room with ID " + chatroomId + " and user ID " + userId);
                }
            } catch (JSONException e) {
                logger.logError("Error parsing JSON message: " + e.getMessage());
            }
        }
    }

    public void broadcastMessage(Long chatroomId, String message) {
        Set<WebSocketSession> sessions = chatRoomSessions.get(chatroomId);
        if (sessions != null) {
            sessions.forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    logger.logError("Error sending message to client: " + e.getMessage());
                }
            });
        }
    }
}