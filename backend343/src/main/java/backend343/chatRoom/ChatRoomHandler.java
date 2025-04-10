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
import java.net.URI;
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
        URI uri = session.getUri();
        if (uri == null) {
            logger.logError("WebSocket session URI is null");
            return;
        }

        String path = uri.getPath();
        String query = uri.getQuery();
        logger.logInfo("WebSocket Connected: " + path + "?" + query);

        String[] parts = path.split("/");
        Long chatroomId = Long.parseLong(parts[parts.length - 1]);

        // Extract userId from query parameters (e.g., "/chat/123?userId=456")
        Long userId = extractUserId(query);
        if (userId == null) {
            logger.logError("UserId is missing in WebSocket connection");
            session.close();
            return;
        }

        // Store userId in session attributes
        session.getAttributes().put("userId", userId);

        // Add session to the chat room
        chatRoomSessions.computeIfAbsent(chatroomId, k -> new CopyOnWriteArraySet<>()).add(session);
        logger.logInfo("WebSocket connection established for user " + userId + " in chatroom " + chatroomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        URI uri = session.getUri();
        if (uri == null) return;

        String[] parts = uri.getPath().split("/");
        Long chatroomId = Long.parseLong(parts[parts.length - 1]);

        Set<WebSocketSession> sessions = chatRoomSessions.get(chatroomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                chatRoomSessions.remove(chatroomId);
            }
        }

        logger.logInfo("WebSocket connection closed for chatroom " + chatroomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.logInfo("Received message: " + message.getPayload());
        URI uri = session.getUri();
        if (uri == null) return;

        String[] parts = uri.getPath().split("/");
        Long chatroomId = Long.parseLong(parts[parts.length - 1]);

        // Get userId from session attributes
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            logger.logError("UserId not found in session attributes");
            return;
        }

        String content = message.getPayload();
        if (!content.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                if (jsonObject.has("message")) {
                    String messageContent = jsonObject.getString("message");
                    chatroomService.sendMessage(chatroomId, userId, messageContent);
                    broadcastMessage(chatroomId, messageContent);
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

    private Long extractUserId(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("userId")) {
                return Long.parseLong(keyValue[1]);
            }
        }
        return null;
    }
}
