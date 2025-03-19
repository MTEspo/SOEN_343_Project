package backend343.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend343.models.User;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserDetailsServiceImpl userDetailsService;

    //total notifications
    @GetMapping("/{userId}/notifications")
    public ResponseEntity<Integer> getTotalNotifications(@PathVariable("userId") Long userId) {
        User user = userDetailsService.getUserById(userId);
        return ResponseEntity.ok(user.getTotalNotifications());
    }

    // notifs for specific chatroom
    @GetMapping("/{userId}/notifications/{chatroomId}")
    public ResponseEntity<Integer> getChatroomNotifications(
            @PathVariable Long userId, @PathVariable("chatroomId") Long chatroomId) {
        User user = userDetailsService.getUserById(userId);
        int count = user.getChatroomNotifications().getOrDefault(chatroomId, 0);
        return ResponseEntity.ok(count);
    }
}
