package backend343.controller;

import backend343.models.User;
import backend343.service.TicketService;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;

    private final TicketService ticketService;

    @PostMapping("/update-username/{userId}")
    public User updateUsername(@PathVariable("userId") Long userId, @RequestParam String newUsername) {
        return userDetailsService.updateUsername(userId, newUsername);
    }

    @GetMapping("/session/{sessionId}/users")
    public ResponseEntity<List<User>> getUsersBySession(@PathVariable("sessionId") Long sessionId) {
        List<User> users = ticketService.getUsersBySessionId(sessionId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}/notifications")
    public ResponseEntity<Integer> getUserTotalNotifications(@PathVariable("userId") Long userId) {
        int totalNotifications = userDetailsService.getTotalNotifications(userId);
        return ResponseEntity.ok(totalNotifications);
    }
}
