package backend343.controller;

import backend343.models.User;
import backend343.service.ChatRoomService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatRoomService chatRoomService;

    //User joins a chatroom
    @PostMapping("/{sessionId}/join/{userId}")
    public void joinChat(@PathVariable Long sessionId, @PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        user.setUsername("User" + userId);

        chatRoomService.joinChat(sessionId, user);
    }

    //User sends a message
    @PostMapping("/{sessionId}/send/{userId}")
    public ResponseEntity<Map<String, String>> sendMessage(@PathVariable Long sessionId, @PathVariable Long userId, @RequestBody String message) {
        User user = new User();
        user.setId(userId);
        user.setUsername("User" + userId);

        chatRoomService.sendMessage(sessionId, user, message);
        return ResponseEntity.ok(Map.of("message", "User " + userId + " sent a message in session " + sessionId));
    }

    //User leaves chatroom
    @PostMapping("/{sessionId}/leave/{userId}")
    public ResponseEntity<Map<String, String>> leaveChat(@PathVariable Long sessionId, @PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        user.setUsername("User" + userId);

        chatRoomService.leaveChat(sessionId, user);
        return ResponseEntity.ok(Map.of("message", "User " + userId + " left chat for session " + sessionId));
    }
}
