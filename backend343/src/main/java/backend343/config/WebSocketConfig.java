package backend343.config;

import backend343.chatRoom.ChatRoomHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatRoomHandler chatRoomHandler;

    @Autowired
    public WebSocketConfig(ChatRoomHandler chatRoomHandler) {
        this.chatRoomHandler = chatRoomHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatRoomHandler, "/chat/*")
                .setAllowedOrigins("https://backend.com", "http://localhost:8080", "http://localhost:3000");
    }
}
