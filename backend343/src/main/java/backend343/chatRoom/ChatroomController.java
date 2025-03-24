package backend343.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend343.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @PostMapping("/{chatroomId}/join/{userId}")
    public ResponseEntity<String> joinChatroom(@PathVariable("chatroomId") Long chatroomId, @PathVariable("userId") Long userId) {
        chatroomService.joinChatroom(chatroomId, userId);
        return ResponseEntity.ok("User joined chatroom");
    }

    @PostMapping("/{chatroomId}/msg/{userId}")
    public ResponseEntity<String> sendMsg(@PathVariable("chatroomId") Long chatroomId, @PathVariable("userId") Long userId, @RequestBody String msg) {
        chatroomService.sendMessage(chatroomId, userId, msg);
        return ResponseEntity.ok("Message sent");
    }

    @PostMapping("/{chatroomId}/leave/{userId}")
    public ResponseEntity<Map<String, Integer>> leaveChatroom(@PathVariable("chatroomId") Long chatroomId, @PathVariable("userId") Long userId) {
        int size = chatroomService.leaveChatroom(chatroomId, userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("numberUsersInChatRoom", size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{chatroomId}/users")
    public ResponseEntity<List<User>> getUsersInChatroom(@PathVariable("chatroomId") Long chatroomId) {
        List<User> users = chatroomService.getUsersInChatroom(chatroomId);
        return ResponseEntity.ok(users);
    }
}
