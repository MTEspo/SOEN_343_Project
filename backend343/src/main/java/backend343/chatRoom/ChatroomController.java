package backend343.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @PostMapping("/{chatroomId}/join/{userId}")
    public ResponseEntity<String> joinChatroom(@PathVariable Long chatroomId, @PathVariable Long userId) {
        chatroomService.joinChatroom(chatroomId, userId);
        return ResponseEntity.ok("User joined chatroom");
    }

    @PostMapping("/{chatroomId}/msg/{userId}")
    public ResponseEntity<String> sendMsg(@PathVariable Long chatroomId, @PathVariable Long userId, @RequestBody String msg) {
        chatroomService.sendMessage(chatroomId, userId, msg);
        return ResponseEntity.ok("Message sent");
    }

    @PostMapping("/{chatroomId}/leave/{userId}")
    public ResponseEntity<Map<String, Integer>> leaveChatroom(@PathVariable Long chatroomId, @PathVariable Long userId) {
        int size = chatroomService.leaveChatroom(chatroomId, userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("numberUsersInChatRoom", size);
        return ResponseEntity.ok(response);
    }
}
