package backend343.chatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{chatroomId}/leave/{userId}")
    public ResponseEntity<String> leaveChatroom(@PathVariable Long chatroomId, @PathVariable Long userId) {
        chatroomService.leaveChatroom(chatroomId, userId);
        return ResponseEntity.ok("User left chatroom");
    }

    @PostMapping("/{chatroomId}/message/{userId}")
    public ResponseEntity<String> sendMessage(
            @PathVariable Long chatroomId,
            @PathVariable Long userId,
            @RequestBody String content) {

        chatroomService.sendMessage(chatroomId, userId, content);
        return ResponseEntity.ok("Message sent");
    }
}
